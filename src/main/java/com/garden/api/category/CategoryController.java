package com.garden.api.category;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CategoryController {

    public static final String BASE_PATH_V1 = "/v1/categories";
    private final CategoryService categoryService;

    @PostMapping(BASE_PATH_V1)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Long categoryId = categoryService.createCategory(categoryRequest);
        return new ResponseEntity<>(categoryId,HttpStatus.CREATED);
    }

    @GetMapping(BASE_PATH_V1)
    public Page<CategoryResponse> findAllCategories(@PageableDefault Pageable pageable) {
        return categoryService.findAllCategories(pageable);
    }

    @GetMapping(BASE_PATH_V1 + "/{categoryId}")
    public ResponseEntity<CategoryResponse> findCategoryById(@PathVariable Long categoryId) {
        CategoryResponse categoryResponse = categoryService.findCategoryById(categoryId);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @DeleteMapping(BASE_PATH_V1 + "/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BASE_PATH_V1 + "/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.updateCategory(categoryId, categoryRequest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(BASE_PATH_V1 + "/search-by-name")
    public ResponseEntity<List<Category>> getCategoriesByName(@RequestParam String name) {
        List<Category> categories = categoryService.getCategoriesByName(name);
        return ResponseEntity.ok(categories);
    }

    @PostMapping(BASE_PATH_V1 + "/{categoryId}/upload-image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadCategoryImage(
            @PathVariable Long categoryId,
            @RequestParam("file") MultipartFile file) {
        String url = categoryService.uploadCategoryImage(categoryId, file);
        return ResponseEntity.ok(url);
    }

    @DeleteMapping(BASE_PATH_V1 + "/{categoryId}/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategoryImage(@PathVariable Long categoryId) {
        categoryService.deleteCategoryImage(categoryId);
        return ResponseEntity.ok().build();
    }

}
