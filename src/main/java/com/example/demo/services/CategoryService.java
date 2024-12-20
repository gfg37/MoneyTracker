package com.example.demo.services;

import com.example.demo.module.Category;
import com.example.demo.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);  // Возвращаем null, если категория не найдена
    }

    @PostConstruct
    public void initializeDefaultCategories() {
        List<String> defaultCategories = Arrays.asList("Продукты", "Аптека", "Переводы", "Одежда и обувь",
                "Кафе и рестораны", "Счета и налоги", "Развлечения", "Транспорт", "Переводы", "Пополнения", "Бонусы", "Остальное");
        for (String categoryName : defaultCategories) {
            if (categoryRepository.findByName(categoryName).isEmpty()) {
                categoryRepository.save(new Category(categoryName));
            }
        }
    }
}
