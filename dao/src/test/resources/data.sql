INSERT INTO Attributes (id, name)
VALUES (1, 'Color'),
       (2, 'Size'),
       (3, 'Material');
INSERT INTO Brands (id, name)
VALUES (1, 'ExampleBrand1'),
       (2, 'ExampleBrand2'),
       (3, 'ExampleBrand3');
INSERT INTO Categories (id, name, description)
VALUES (1, 'Electronics', 'Electronic gadgets and devices'),
       (2, 'Clothing', 'Clothing items and accessories'),
       (3, 'Home Appliances', 'Appliances for home use');
INSERT INTO Addresses (id, address_line, city, postal_code, country_id)
VALUES (1, '123 Main St', 'New York', '10001', 1),
       (2, '456 High St', 'London', 'SW1A 1AA', 2);
INSERT INTO post_addresses (id, city, department)
VALUES
    (1, 'New York', '11'),
    (2, 'Los Angeles', '22'),
    (3, 'Chicago', '33');
INSERT INTO Users (id, password, first_name, last_name, phone_number, email, created_at, role, refresh_token_key, address_id)
VALUES (1, 'password1', 'John', 'Doe', '+1234567890', 'john@example.com', '2024-05-10 08:00:00', 'ROLE_USER', '3cZAVF/SKSCmCM2+1azD2XHK7K2PChcSg32vrrEh/Qk=', 1),
       (2, 'password2', 'Jane', 'Smith', '+1987654321', 'jane@example.com', '2024-05-10 09:00:00', 'ROLE_USER', '4cZAVF/SKSCmCM2+1azD2XHK7K2PChcSg32vrrEh/Qk=', 2);
INSERT INTO Products (id, short_desc, category_id, brand_id, name, description, price, quantity, created_at)
VALUES (1, 'Smartphone', 1, 1, 'Example Smartphone', 'This is an example smartphone.', 599.99, 100,
        '2024-05-10 10:00:00'),
       (2, 'T-shirt', 2, 2, 'Example T-shirt', 'This is an example t-shirt.', 19.99, 200, '2024-05-10 11:00:00');
INSERT INTO Images (id, product_id, link, image_order)
VALUES (1, 1, 'https://example.com/image1.jpg', 1),
       (2, 2, 'https://example.com/image2.jpg', 1);
INSERT INTO Orders (id, created_at, first_name, last_name, email, phone_number, is_paid, delivery_status, delivery_method, address_id, post_address_id)
VALUES (1, '2024-07-04 12:00:00', 'John', 'Doe', 'john.doe@example.com', '1234567890', TRUE, 'COMPLETED', 'NOVA', null, 1),
       (2, '2024-07-03 12:00:00', 'Johana', 'Doe', 'johana.doe@example.com', '1234567891', FALSE, 'PROCESSING', 'COURIER', 1, null);
INSERT INTO Order_Items (order_id, product_id, quantity, price)
VALUES (1, 1, 1,40.99),
       (2, 2, 2,400.99);
INSERT INTO Cart_items (user_id, product_id, quantity, created_at)
VALUES (1, 2, 1, '2024-05-10 14:00:00'),
       (2, 1, 2, '2024-05-10 15:00:00');
INSERT INTO Attribute_Values (id, attribute_id, value)
VALUES (1, 1, 'Red'),
       (2, 1, 'Blue'),
       (3, 2, 'Small'),
       (4, 2, 'Large'),
       (5, 3, 'Cotton'),
       (6, 3, 'Polyester');
INSERT INTO Product_Attributes (id, product_id, attribute_value_id)
VALUES (1, 1, 1),
       (2, 1, 5),
       (3, 2, 2),
       (4, 2, 6);
INSERT INTO Category_Attributes (id, category_id, attribute_id)
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 2, 1),
       (4, 2, 3),
       (5, 3, 3);
INSERT INTO users_orders (user_id, order_id)
VALUES (1, 1);
INSERT INTO reviews (user_id, product_id, text, rate, created_at)
VALUES (1,1,'sometext',2,'2024-05-10 14:00:00'),
       (2,2,'sometext',4,'2024-04-10 14:00:00');