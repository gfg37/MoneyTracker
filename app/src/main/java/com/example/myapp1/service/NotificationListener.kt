package com.example.myapp1.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myapp1.R
import com.example.myapp1.model.CategoryRequest
import com.example.myapp1.model.TransactionRequest
import com.example.myapp1.model.UserRequest
import com.example.myapp1.serv.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

class NotificationListener : NotificationListenerService() {

    override fun onCreate() {
        super.onCreate()
        Log.d("NotificationListener", "Сервис успешно запущен")
        startForegroundService()
    }

    private fun startForegroundService() {
        val channelId = "notification_listener_service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notification Listener",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Listening for notifications")
            .setContentText("Notification listener is active.")
            .setSmallIcon(R.drawable.icon)
            .build()

        startForeground(1, notification) // Служба работает в фоновом режиме
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        try {
            Log.d("NotificationListener", "Получено уведомление: ${sbn.packageName}")

            val notification = sbn.notification
            val extras = notification.extras
            val message = extras.getCharSequence("android.text")?.toString()
            val packageName = sbn.packageName

            if (message == null || message.isBlank()) {
                Log.d("NotificationListener", "Текст уведомления пустой")
                return
            }

            Log.d("NotificationListener", "Текст уведомления: $message")

            if (packageName != "com.tbank") { // Убедитесь, что это уведомление от Т-банка
                Log.d("NotificationListener", "Не Т-банк, игнорируем уведомление.")
                return
            }

            // Если уведомление от Т-банка, обработаем его
            if (message.contains("Перевод на", ignoreCase = true)) {
                val amount = extractAmount(message)
                if (amount != null) {
                    createTransaction(amount, message)
                } else {
                    Log.d("NotificationListener", "Не удалось извлечь сумму из уведомления")
                }
            } else {
                Log.d("NotificationListener", "Уведомление не содержит ключевого слова")
            }
        } catch (e: Exception) {
            Log.e("NotificationListener", "Ошибка при обработке уведомления: ${e.message}", e)
        }
    }

    private fun extractAmount(message: String): Double? {
        return try {
            val pattern = Pattern.compile("Перевод на (\\d+(?:\\.\\d+)?)")
            val matcher = pattern.matcher(message)
            if (matcher.find()) {
                matcher.group(1)?.toDouble()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("NotificationListener", "Ошибка извлечения суммы: ${e.message}", e)
            null
        }
    }

    private fun createTransaction(amount: Double, message: String) {
        try {
            val userId = 1L // Укажите актуальный userId
            val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val signedAmount = -amount

            val transaction = TransactionRequest(
                amount = signedAmount,
                description = message,
                dateTime = currentDateTime,
                category = CategoryRequest(11L), // ID категории "Остальное"
                user = UserRequest(userId)
            )

            RetrofitClient.api.createTransaction(transaction).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("NotificationListener", "Транзакция добавлена успешно!")
                    } else {
                        Log.e(
                            "NotificationListener",
                            "Ошибка сервера: ${response.code()} - ${response.errorBody()?.string()}"
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("NotificationListener", "Ошибка сети: ${t.message}", t)
                }
            })
        } catch (e: Exception) {
            Log.e("NotificationListener", "Ошибка создания транзакции: ${e.message}", e)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("NotificationListener", "Уведомление удалено: ${sbn.packageName}")
    }
}
