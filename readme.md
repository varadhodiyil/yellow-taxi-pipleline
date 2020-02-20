# Data Engineering for Distributed Micro-Service Pipeline

This repository contains the yellow-taxi pipepline code files for both Java and Python.

## Application file format

The `yellow-taxi-pipleline` directory contains code for python services.

- `yellowTaxiApi`: root directory for yellowTaxiAPI

The `yellow-taxi-pipleline/yellowtaxipipleline` directory contains code for Java services.

- `com.yellowtaxipipeline`: contains Consumer, Producer classes and files to read local csv files
- `com.yellowtaxipipeline.model`: contains model files for underlying business entitles

## How to Run the Application
### Installing Dependencies

We've listed the necessary requirements required to run the applications in requirements.txt. To install the dependencies
```
pip install -r requirements.txt
```    

- Start ActiveMQ server

    ```
    activemq start
    ```
- Run Python application to load and publish intial trip and crash data. 
    ```
    python test_prod.py
    ```
- Run Java application to consume trip and crash data, process it and publish it back
    ```
    java App.java
    ```
- Run Reports from python to show hourly and daily window.
    ```
    python report.py
    ```
- To start the API server
    ```
    cd yellowTaxiApi
    python manage.py runserver
    ```

