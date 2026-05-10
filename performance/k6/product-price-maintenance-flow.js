import http from 'k6/http';
import { check } from 'k6';
import { sleep } from 'k6';

export const options = {
  vus: 5,
  duration: '30s',
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<700'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export function setup() {
  waitForApi();
}

export default function () {
  const suffix = `${Date.now()}-${__VU}-${__ITER}`;
  const productId = createProduct(suffix);

  if (!productId) {
    return;
  }

  const priceId = addPrice(productId);

  if (!priceId) {
    return;
  }

  const historyResponse = http.get(
    `${BASE_URL}/products/${productId}/prices?page=0&size=10&sort=initDate,asc`,
    {
      tags: {
        name: 'GET /products/{id}/prices',
      },
    }
  );

  check(historyResponse, {
    'history returned': (response) => response.status === 200,
  });

  const updatePriceResponse = http.put(
    `${BASE_URL}/products/${productId}/prices/${priceId}`,
    JSON.stringify({
      value: 109.99,
      currency: 'USD',
      initDate: '2042-01-01',
      endDate: '2042-12-31',
    }),
    jsonHeaders('PUT /products/{productId}/prices/{priceId}')
  );

  check(updatePriceResponse, {
    'price updated': (response) => response.status === 200,
  });

  const deletePriceResponse = http.del(
    `${BASE_URL}/products/${productId}/prices/${priceId}`,
    null,
    {
      tags: {
        name: 'DELETE /products/{productId}/prices/{priceId}',
      },
    }
  );

  check(deletePriceResponse, {
    'price deleted': (response) => response.status === 204,
  });
}

function createProduct(suffix) {
  const response = http.post(
    `${BASE_URL}/products`,
    JSON.stringify({
      name: `k6 maintenance product ${suffix}`,
      description: 'Created from k6 maintenance flow',
    }),
    jsonHeaders('POST /products')
  );

  check(response, {
    'product created': (result) => result.status === 201,
  });

  if (response.status !== 201) {
    return null;
  }

  return response.json('id');
}

function addPrice(productId) {
  const response = http.post(
    `${BASE_URL}/products/${productId}/prices`,
    JSON.stringify({
      value: 99.99,
      currency: 'EUR',
      initDate: '2042-01-01',
      endDate: '2042-12-31',
    }),
    jsonHeaders('POST /products/{id}/prices')
  );

  check(response, {
    'price created': (result) => result.status === 201,
  });

  if (response.status !== 201) {
    return null;
  }

  return response.json('id');
}

function jsonHeaders(name) {
  return {
    headers: {
      'Content-Type': 'application/json',
    },
    tags: {
      name,
    },
  };
}

function waitForApi() {
  for (let attempt = 0; attempt < 60; attempt += 1) {
    const response = http.get(`${BASE_URL}/v3/api-docs`, {
      tags: {
        name: 'GET /v3/api-docs readiness',
      },
    });

    if (response.status === 200) {
      return;
    }

    sleep(1);
  }

  throw new Error(`API is not available at ${BASE_URL}`);
}
