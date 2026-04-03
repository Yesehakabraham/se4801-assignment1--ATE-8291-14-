// Student Number: [ATE/8291/14]
// Student Name : YESEHAK ABRAHAM
package com.shopwave.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super("Category not found with id: " + id);
    }
}
