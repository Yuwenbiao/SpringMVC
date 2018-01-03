package validator;

import domain.Product;
import form.ProductForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//public class ProductValidator {
//    public List<String> validate(ProductForm productForm) {
//        List<String> errors = new ArrayList<>();
//        String name = productForm.getName();
//        if (name == null || name.trim().isEmpty()) {
//            errors.add("Product must have a name");
//        }
//        String price = productForm.getPrice();
//        if (price == null || price.trim().isEmpty()) {
//            errors.add("Product must have a price");
//        } else {
//            try {
//                Float.parseFloat(price);
//            } catch (NumberFormatException e) {
//                errors.add("Invalid price value");
//            }
//        }
//        return errors;
//    }
//}
public class ProductValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Product.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Product product = (Product) o;
        ValidationUtils.rejectIfEmpty(errors, "name", "productname.required");
        ValidationUtils.rejectIfEmpty(errors, "price", "price.required");
        ValidationUtils.rejectIfEmpty(errors, "productionDate", "productiondate.required");
        Float price = product.getPrice();
        if (price != null && price < 0) {
            errors.rejectValue("price", "price.negative");
        }
        Date productionDate = product.getProductionDate();
        if (productionDate != null) {
            if (productionDate.after(new Date())) {
                System.out.println("salah lagi");
                errors.rejectValue("productionDate", "productiondate.invalid");
            }
        }
    }
}