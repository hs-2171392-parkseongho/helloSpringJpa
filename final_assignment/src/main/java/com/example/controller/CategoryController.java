// category 등록 기능 구현

package com.example.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import com.example.service.CategoryService;
import com.example.model.CategoryForm;
import com.example.exception.DuplicateCategoryException;

@Controller
@RequestMapping("/categories")
public class CategoryController{
 private final CategoryService categoryService;
 public CategoryController(CategoryService categoryService){
  this.categoryService=categoryService;
 }
 @GetMapping
 public String list(Model model){
  model.addAttribute("categories",categoryService.getAllCategories());
  return "categoryList";
 }
 @GetMapping("/create")
 public String form(Model model){
  model.addAttribute("categoryForm",new CategoryForm());
  return "categoryForm";
 }
 @PostMapping("/create")
 public String create(@Valid @ModelAttribute CategoryForm form, BindingResult br, RedirectAttributes ra){
  if(br.hasErrors()) return "categoryForm";
  try{
   categoryService.createCategory(form.getName());
   ra.addFlashAttribute("successMessage","등록 완료");
  }catch(DuplicateCategoryException e){
   br.rejectValue("name","dup",e.getMessage());
   return "categoryForm";
  }
  return "redirect:/categories";
 }
 @PostMapping("/{id}/delete")
 public String delete(@PathVariable Long id, RedirectAttributes ra){
  try{
   categoryService.deleteCategory(id);
   ra.addFlashAttribute("successMessage","삭제 완료");
  }catch(Exception e){
   ra.addFlashAttribute("errorMessage",e.getMessage());
  }
  return "redirect:/categories";
 }
}
