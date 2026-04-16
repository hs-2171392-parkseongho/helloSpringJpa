
package com.example.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import com.example.repository.CategoryRepository;
import com.example.exception.DuplicateCategoryException;
import com.example.model.Category;

@Service
@Transactional(readOnly=true)
public class CategoryService {
 private final CategoryRepository categoryRepository;
 public CategoryService(CategoryRepository categoryRepository){
  this.categoryRepository=categoryRepository;
 }
 public List<Category> getAllCategories(){
  return categoryRepository.findAll();
 }
 @Transactional
 public Category createCategory(String name){
  categoryRepository.findByName(name)
   .ifPresent(c->{throw new DuplicateCategoryException(name);});
  return categoryRepository.save(new Category(name));
 }
 @Transactional
 public void deleteCategory(Long id){
  long count=categoryRepository.countProductsByCategoryId(id);
  if(count>0) throw new IllegalStateException("상품 "+count+"개 연결됨");
  categoryRepository.delete(id);
 }
}
