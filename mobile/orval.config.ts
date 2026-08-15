import { defineConfig } from 'orval';

export default defineConfig({
  anverra: {
    input: '../frontend/openapi.json',
    output: {
      mode: 'split',
      target: 'src/api/generated/endpoints.ts',
      schemas: 'src/api/generated/models',
      client: 'react-query',
      override: {
        mutator: {
          path: 'src/api/mutator/custom-instance.ts',
          name: 'customInstance'
        }
      }
    }
  }
});
