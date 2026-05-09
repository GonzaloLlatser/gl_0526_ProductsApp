package com.gft.products.e2e;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductPricesE2ETest {

  @LocalServerPort
  void setPort(int port) {
    RestAssured.port = port;
  }

  @Test
  void shouldCreateProductSuccessfully() {
    given()
        .contentType(ContentType.JSON)
        .body(Map.of(
            "name", "E2E Product",
            "description", "Created from e2e test"
        ))
        .when()
        .post("/products")
        .then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("name", equalTo("E2E Product"))
        .body("description", equalTo("Created from e2e test"));
  }

  @Test
  void shouldAddPriceSuccessfully() {
    Long productId = createProduct("E2E Price Product");

    given()
        .contentType(ContentType.JSON)
        .body(priceRequest("79.99", "2030-01-01", "2030-12-31"))
        .when()
        .post("/products/{id}/prices", productId)
        .then()
        .statusCode(201)
        .body("value", comparesEqualTo(79.99F))
        .body("initDate", equalTo("2030-01-01"))
        .body("endDate", equalTo("2030-12-31"));
  }

  @Test
  void shouldRejectPriceWithInvalidDateRange() {
    Long productId = createProduct("E2E Invalid Range Product");

    given()
        .contentType(ContentType.JSON)
        .body(priceRequest("79.99", "2031-01-01", "2031-01-01"))
        .when()
        .post("/products/{id}/prices", productId)
        .then()
        .statusCode(400)
        .body("code", equalTo("INVALID_PRICE_DATE_RANGE"));
  }

  @Test
  void shouldRejectOverlappingPrice() {
    Long productId = createProduct("E2E Overlap Product");
    addPrice(productId, "89.99", "2032-01-01", "2032-12-31");

    given()
        .contentType(ContentType.JSON)
        .body(priceRequest("99.99", "2032-06-01", "2033-01-31"))
        .when()
        .post("/products/{id}/prices", productId)
        .then()
        .statusCode(409)
        .body("code", equalTo("PRICE_DATE_OVERLAP"));
  }

  @Test
  void shouldGetCurrentPriceByDate() {
    Long productId = createProduct("E2E Current Price Product");
    addPrice(productId, "49.99", "2033-01-01", "2033-12-31");

    given()
        .queryParam("date", "2033-04-15")
        .when()
        .get("/products/{id}/prices", productId)
        .then()
        .statusCode(200)
        .body("value", comparesEqualTo(49.99F));
  }

  @Test
  void shouldReturnNotFoundWhenThereIsNoCurrentPriceForDate() {
    Long productId = createProduct("E2E Missing Current Price Product");
    addPrice(productId, "59.99", "2034-01-01", "2034-12-31");

    given()
        .queryParam("date", "2035-04-15")
        .when()
        .get("/products/{id}/prices", productId)
        .then()
        .statusCode(404)
        .body("code", equalTo("PRICE_NOT_FOUND"));
  }

  @Test
  void shouldGetCompletePriceHistory() {
    Long productId = createProduct("E2E History Product");
    addPrice(productId, "19.99", "2036-01-01", "2036-06-30");
    addPrice(productId, "29.99", "2036-07-01", null);

    given()
        .when()
        .get("/products/{id}/prices", productId)
        .then()
        .statusCode(200)
        .body("name", equalTo("E2E History Product"))
        .body("prices", hasSize(2))
        .body("prices[0].value", comparesEqualTo(19.99F))
        .body("prices[0].initDate", equalTo("2036-01-01"))
        .body("prices[0].endDate", equalTo("2036-06-30"))
        .body("prices[1].value", comparesEqualTo(29.99F))
        .body("prices[1].initDate", equalTo("2036-07-01"))
        .body("prices[1].endDate", equalTo(null));
  }

  private Long createProduct(String name) {
    return given()
        .contentType(ContentType.JSON)
        .body(Map.of(
            "name", name,
            "description", "Created from e2e test"
        ))
        .when()
        .post("/products")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getLong("id");
  }

  private void addPrice(Long productId, String value, String initDate, String endDate) {
    given()
        .contentType(ContentType.JSON)
        .body(priceRequest(value, initDate, endDate))
        .when()
        .post("/products/{id}/prices", productId)
        .then()
        .statusCode(201);
  }

  private Map<String, Object> priceRequest(String value, String initDate, String endDate) {
    Map<String, Object> request = new HashMap<>();
    request.put("value", new BigDecimal(value));
    request.put("initDate", LocalDate.parse(initDate));
    request.put("endDate", endDate == null ? null : LocalDate.parse(endDate));
    return request;
  }
}
