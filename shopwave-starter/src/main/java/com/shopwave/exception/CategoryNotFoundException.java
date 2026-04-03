// Student Number: [YOUR_STUDENT_NUMBER]
package com.shopwave.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super("Category not found with id: " + id);
    }
}
