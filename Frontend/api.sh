#!/bin/bash
rm -rf api 
npx @openapitools/openapi-generator-cli generate \
   -i http://localhost:8080/v3/api-docs \
   -g typescript-axios \
   -o api