package edu.thejoeun.product.controller;

import edu.thejoeun.product.model.dto.Product;
import edu.thejoeun.product.model.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * 전체 상품 조회
     * 전체 상품 조회가 성공 ResponseEntity.ok = 200
     * 번호에 따른 상태 확인.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("✅ GET /api/product/all - 전체 상품 조회");
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 상품 상세 조회
     * @param id 아이디를 통해 조회
     * @return id에 해당하는 제품 데이터 반환
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {
        log.info("✅ GET /api/product/{} - 상품 상세 조회", id);
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch(Exception e) {
            Map<String, Object> res = new HashMap<>();
            res.put("success", false);
            res.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    /**
     * PathVariable & RequestParam = Header 에서 데이터 주고받기
     * PathVariable = 중괄호{} 형태로, {} 내부에 변수명에 해당하는 데이터로 접근
     * RequestParam = ?category="카테고리명칭" 과 같은 형태로 키:값 데이터로 접근
     * @param category 클라이언트가 클릭한 카테고리명
     * @return 카테고리에 해당하는 상품들 조회
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        log.info("✅ GET /api/product/category/{} - 카테고리별 조회", category);
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    /**
     * PathVariable & RequestParam = Header 에서 데이터 주고받기
     * PathVariable = 중괄호{} 형태로, {} 내부에 변수명에 해당하는 데이터로 접근
     * RequestParam = ?category="카테고리명칭" 과 같은 형태로 키:값 데이터로 접근
     * @param keyword 키워드에 해당하는 데이터를 DB에 조회 후
     * @return 검색하고 조회된 모든 데이터를 목록 형태로 반환
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        log.info("✅ GET /api/product/search?keyword={} - 상품 검색", keyword);
        List<Product> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    /**
     *
     * @param product  @RequestPart() 내부에는 javascript 에서 지정한 변수명과 required 형태를 지정하여 작성할 수 있다.
     *                 만일 아무것도 작성하지 않을 경우에는 백엔드에서 사용하는 변수명과 프론트엔드에서 사용하는 변수명이 일치하고,
     *                 모든 데이터를 필수로 전달받는 변수명칭이라는 표기.
     *                 @RequestPart("prdct" required = false) Product product
     *                 @RequestPart(value="prdct" require = false) Product product  (위 아래 모두 동일한 형태)
     *                 -> 이 경우에는 프론트엔드에서 변수이름이 prdct 이고, 필수로 데이터를 가져와 product 내부에 추가하지 않아도 될 때(require=false) 사용하는 표기법.
     * @param imageFile     백엔드에서는 file 변수명으로 imageFile 로 프론트엔드에서 가져온 데이터를 전달받을 것이며,
     *                      데이터는 required = false 필수로 들어있지 않아도 된다.
     * @return         성공 결과 여부를
     *                   Map< String   ,  Object >
     *                      "success"  : boolean
     *                      "message"  : "결과에 대한 메세지"
     *                      "productId": 필요하다면 등록된 제품 아이디 숫자값
     *                 으로 프론트엔드에 반환할 것.
     *                 프론트엔드에서는
     *                      [백엔드 성공여부 변수이름].data.success
     *                      [백엔드 성공여부 변수이름].data.message
     *                      [백엔드 성공여부 변수이름].data.productId
 *                     와 같은 형태로 사용할 수 있다.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> addProduct(@RequestPart("product") Product product,
                                                          @RequestPart(value="imageFile", required = false) MultipartFile imageFile) {
        log.info("💡 POST /api/product- 상품 등록: {}", product.getProductName());
        Map<String, Object> res = new HashMap<>();

        try {
            productService.insertProduct(product, imageFile);
            res.put("success", true);
            res.put("message", "상품이 성공적으로 등록되었습니다.");
            res.put("productId", product.getId());
            log.info("✅ 상품 등록 성공 - ID: {}", product.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(res);

        } catch (IllegalArgumentException e) {
            log.warn("⚠️ 상품 등록 실패 - 유효성 검사 오류: {}", e.getMessage());
            res.put("success", false);
            res.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(res);

        } catch(Exception e) {
            log.error("❌ 상품 등록 실패 - 서버 오류", e);
            res.put("success", false);
            res.put("message", "상품 등록 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);

        }
    }

    /**
     * 상품 수정
     * @param id        수정할 제품의 id 가져오기
     * @param product   수정할 제품에 대해 작성된 내용 모두 가져오기
     * @return          수정된 결과 클라이언트에게 전달
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable int id, @RequestBody Product product) {
        log.info("✅ PUT /api/product/{} - 상품 수정", id);
        Map<String, Object> res = new HashMap<>();

        try {
            product.setId(id);
            productService.updateProduct(product);
            res.put("success", true);
            res.put("message", "상품이 성공적으로 수정되었습니다.");
            res.put("productId", product.getId());
            log.info("✅ 상품 수정 성공 - ID: {}", product.getId());
            return ResponseEntity.ok(res);

        } catch (IllegalArgumentException e) {
            log.warn("⚠️ 상품 수정 실패 - 유효성 검사 오류: {}", e.getMessage());
            res.put("success", false);
            res.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(res);

        } catch(Exception e) {
            log.error("❌ 상품 수정 실패 - 서버 오류", e);
            res.put("success", false);
            res.put("message", "상품 수정 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);

        }
    }

    /**
     * 상품 삭제
     * @param id    id에 해당하는 상품 삭제 관련 기능 수행
     * @return      수행 결과 반환
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable int id) {
        log.info("✅ DELETE /api/product/{} - 상품 삭제", id);
        Map<String, Object> res = new HashMap<>();

        try {
            productService.deleteProduct(id);
            res.put("success", true);
            res.put("message", "상품이 성공적으로 삭제되었습니다.");
            res.put("productId", id);
            log.info("✅ 상품 삭제 성공 - ID: {}", id);
            return ResponseEntity.ok(res);

        } catch (IllegalArgumentException e) {
            log.warn("⚠️ 상품 삭제 실패 - 유효성 검사 오류: {}", e.getMessage());
            res.put("success", false);
            res.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(res);

        } catch(Exception e) {
            log.error("❌ 상품 삭제 실패 - 서버 오류", e);
            res.put("success", false);
            res.put("message", "상품 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);

        }
    }

    /**
     * 재고 업데이트
     * @param id                재고 업데이트할 상품 id 조회
     * @RequestParam quantity   프론트엔드에서 재고 업데이트 관련 수량 변경 요청
     * @return                  요청 결과 반환
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Map<String, Object>> updateStock(@PathVariable int id, @RequestParam int quantity) {
        log.info("✅ PATCH /api/product/{}/stock?quantity={} - 재고 업데이트", id, quantity);
        Map<String, Object> res = new HashMap<>();

        try {
            productService.updateStock(id, quantity);
            res.put("success", true);
            res.put("message", "재고가 성공적으로 업데이트되었습니다.");
            res.put("productId", id);
            log.info("✅ 재고 업데이트 성공 - ID: {}", id);
            return ResponseEntity.ok(res);

        } catch (IllegalArgumentException e) {
            log.warn("⚠️ 재고 업데이트 실패 - 유효성 검사 오류: {}", e.getMessage());
            res.put("success", false);
            res.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(res);

        } catch(Exception e) {
            log.error("❌ 재고 업데이트 실패 - 서버 오류", e);
            res.put("success", false);
            res.put("message", "재고 업데이트 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);

        }
    }
}
