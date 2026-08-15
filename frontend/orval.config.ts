import { defineConfig } from 'orval';

export default defineConfig({
  api: {
    input: './openapi.json',
    output: {
      mode: 'tags-split',
      target: 'src/api/endpoints',
      schemas: 'src/api/model',
      client: 'react-query',
      mock: true,
      override: {
        mutator: {
          path: 'src/api/mutator/custom-instance.ts',
          name: 'customInstance',
        },
      },
    },
  },
});
