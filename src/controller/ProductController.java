package controller;

import domain.Product;
import form.ProductForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.ProductService;
import validator.ProductValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {
    private static final Log logger = LogFactory.getLog(ProductController.class);
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/product_input")
    public String inputProduct() {
        logger.info("inputProduct called");
        return "ProductForm";
    }
//
//    @RequestMapping(value = "/product_save", method = RequestMethod.POST)
//    public String saveProduct(ProductForm productForm, RedirectAttributes redirectAttributes) {
//        logger.info("saveProduct called");
//        Product product = new Product();
//        product.setName(productForm.getName());
//        product.setDescription(productForm.getDescription());
//        try {
//            product.setPrice(Float.parseFloat(productForm.getPrice()));
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//
//        //add product
//        Product savedProduct = productService.add(product);
//        redirectAttributes.addFlashAttribute("message", "The product was successfully added");
//        return "redirect:/product_view/" + savedProduct;
//    }

    @RequestMapping(value = "/product_save", method = RequestMethod.POST)
    public String saveProduct(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        List<MultipartFile> files = product.getImages();
        List<String> fileNames = new ArrayList<>();
        if (files != null && files.size() > 0) {
            for (MultipartFile multipartFile : files) {
                String fileName = multipartFile.getOriginalFilename();
                fileNames.add(fileName);
                File imageFile = new File(request.getServletContext().getRealPath("/image"), fileName);
                try {
                    multipartFile.transferTo(imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        ProductValidator productValidator = new ProductValidator();
//        productValidator.validate(product, bindingResult);
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            logger.info("code:" + fieldError.getCode() + ",field:" + fieldError.getField());
            return "ProductForm";
        }
        logger.info("saveProduct called");
        product.setName(product.getName());
        product.setDescription(product.getDescription());
        product.setPrice(product.getPrice());

        //add product
        Product savedProduct = productService.add(product);
        redirectAttributes.addFlashAttribute("message", "The product was successfully added");
        return "redirect:/product_view/" + savedProduct;
    }

    @RequestMapping(value = "/product_view/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productService.get(id);
        model.addAttribute("product", product);
        return "ProductView";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new ProductValidator());
        binder.validate();
    }
}
