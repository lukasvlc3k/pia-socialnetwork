FROM node:16-alpine as build
WORKDIR /app

COPY package.json package.json
COPY yarn.lock yarn.lock
RUN yarn install --pure-lockfile --link-duplicates

COPY . .

RUN yarn run build

FROM node:16-alpine
WORKDIR /app

COPY package.json package.json
COPY yarn.lock yarn.lock
RUN yarn install --pure-lockfile --production --link-duplicates

COPY --from=build /app/.next .next
COPY --from=build /app/public public

EXPOSE 3000
CMD yarn start -p 3000
