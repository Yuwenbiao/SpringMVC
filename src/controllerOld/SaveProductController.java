package controllerOld;

import domain.Product;
import form.ProductForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveProductController implements Controller {
    private static final Log logger = LogFactory.getLog(SaveProductController.class);

    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        logger.info("SaveProductController called");
        ProductForm productForm = new ProductForm();
        //populate action properties
        productForm.setName(httpServletRequest.getParameter("name"));
        productForm.setDescription(httpServletRequest.getParameter("description"));
        productForm.setPrice(httpServletRequest.getParameter("price"));

        //create model
        Product product = new Product();
        product.setName(productForm.getName());
        product.setDescription(productForm.getDescription());
        try {
            product.setPrice(Float.parseFloat(productForm.getPrice()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        //insert code to save Product
        return new ModelAndView("/WEB-INF/jsp/ProductDetails.jsp", "product", product);
    }
}
