import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp1.R
import com.example.myapp1.model.TransactionHistoryItem

class TransactionHistoryAdapter(
    private val transactions: List<TransactionHistoryItem>
) : RecyclerView.Adapter<TransactionHistoryAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_history_item, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int = transactions.size

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val itemContainer: LinearLayout = itemView.findViewById(R.id.itemContainer)

        fun bind(transaction: TransactionHistoryItem) {
            amountTextView.text = "${transaction.amount} ₽" // Добавляем валюту
            dateTextView.text = transaction.date
            descriptionTextView.text = transaction.description ?: "Без описания"

            // Устанавливаем фон в зависимости от categoryId, используя безопасный доступ
            val backgroundResId = when (transaction.categoryId) {
                1L -> R.drawable.fir
                4L -> R.drawable.two
                3L -> R.drawable.dvenat
                2L -> R.drawable.chet
                5L -> R.drawable.vos
                6L -> R.drawable.piat
                7L -> R.drawable.six
                8L -> R.drawable.sem
                9L -> R.drawable.tri
                10L -> R.drawable.odina


                else -> R.drawable.normis // Установим стандартный фон
            }
            itemContainer.setBackgroundResource(backgroundResId)
        }
    }
}
