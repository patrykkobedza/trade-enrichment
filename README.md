h
### How to run:
start as springboot app: `mvn spring-boot:run`. You can also use one of 3 predefined static products lists used to enrich received trades, by default 10 product list is loaded,
you can switch used list by passing one of provided filenames `(product.csv, product_10k.csv, product_100k.csv)` as argument:

```mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dproduct.staticlist.filename=product_10k.csv'```

### How to use:
Api accepts multipartFile requests as input, two example input files `(trades.csv, trades_bulk.csv)` for request are available in resources, and can be sent using curl:
```curl
curl -F "file=@src/test/resources/trades.csv" http://localhost:8080/api/v1/enrich
```
2nd provided file `(trades_bulk.csv)` has 3 000 0000 records. 


### Limitations:
Columns in sent file must be in correct order: `date,product_id,currency,price`


### Comments:
Products static list is stored in Hashmap for o(1) read complexity, even edge case 100k products shouldn't consume too much memory. Code is threadsafe - used hashmap is modified only once, at startup.
Concurrent get() calls are thread safe. 
