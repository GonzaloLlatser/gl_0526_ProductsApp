import http from 'k6/http';
import { check } from 'k6';
import { sleep } from 'k6';

export const options = {
  vus: 10,
  duration: '30s',
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<500'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export function setup() {
  waitForApi();
}

export default function () {
  const suffix = `${Date.now()}-${__VU}-${__ITER}`;

  const createProductResponse = http.post(
    `${BASE_URL}/products`,
    JSON.stringify({
      name: `k6 basic product ${suffix}`,
      description: 'Created from k6 basic flow',
    }),
    jsonHeaders('POST /products')
  );

  check(createProductResponse, {
    'product created': (response) => response.status === 201,
  });

  if (createProductResponse.status !== 201) {
    return;
  }

  const productId = createProductResponse.json('id');
  if (!productId) {
    return;
  }

  const addPriceResponse = http.post(
    `${BASE_URL}/products/${productId}/prices`,
    JSON.stringify({
      value: 99.99,
      currency: 'EUR',
      initDate: '2041-01-01',
      endDate: '2041-12-31',
    }),
    jsonHeaders('POST /products/{id}/prices')
  );

  check(addPriceResponse, {
    'price created': (response) => response.status === 201,
  });

  const getCurrentPriceResponse = http.get(
    `${BASE_URL}/products/${productId}/prices?date=2041-04-15`,
    {
      tags: {
        name: 'GET /products/{id}/prices?date',
      },
    }
  );

  check(getCurrentPriceResponse, {
    'current price returned': (response) => response.status === 200,
  });
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
