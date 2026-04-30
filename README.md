# 🍔 Food Ordering Platform — Backend

<div align="center">

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.4-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.x-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-0.11.5-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-20.107-635BFF?style=for-the-badge&logo=stripe&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

**RESTful API Backend** cho nền tảng đặt món ăn trực tuyến — xây dựng bằng **Spring Boot 3** với bảo mật JWT đầy đủ, phân quyền RBAC theo vai trò, luồng đặt hàng hoàn chỉnh và tích hợp thanh toán trực tuyến qua **Stripe**.

[🌐 Frontend Repo](https://github.com/NgSang0127/foodorderingui) · [👤 Tác giả](https://github.com/NgSang0127) · [🐛 Báo lỗi](https://github.com/NgSang0127/FoodOrderingWebBE/issues)

</div>

---

## 📋 Mục lục

- [Tổng quan dự án](#-tổng-quan-dự-án)
- [Tính năng chính](#-tính-năng-chính)
- [Tech Stack](#-tech-stack)
- [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
- [Cấu trúc dự án](#-cấu-trúc-dự-án)
- [API Endpoints](#-api-endpoints)
- [Luồng xác thực JWT](#-luồng-xác-thực-jwt)
- [Luồng đặt hàng & thanh toán](#-luồng-đặt-hàng--thanh-toán)
- [Thiết kế Database](#-thiết-kế-database)
- [Yêu cầu hệ thống](#-yêu-cầu-hệ-thống)
- [Hướng dẫn cài đặt & chạy](#-hướng-dẫn-cài-đặt--chạy)
- [Cấu hình Application](#-cấu-hình-application)
- [Đóng góp](#-đóng-góp)
- [Tác giả](#-tác-giả)

---

## 🎯 Tổng quan dự án

**FoodOrderingWebBE** là backend RESTful API cho hệ thống đặt món ăn trực tuyến — một dự án full-stack thực hành của **Nguyễn Công Sáng** nhằm xây dựng nền tảng thương mại điện tử trong lĩnh vực F&B (Food & Beverage). Hệ thống hỗ trợ ba nhóm người dùng với phân quyền rõ ràng:

- **Customer (Khách hàng)**: Đăng ký, đăng nhập, duyệt menu nhà hàng, đặt hàng, theo dõi trạng thái đơn, quản lý hồ sơ và địa chỉ giao hàng.
- **Restaurant Owner (Chủ nhà hàng)**: Đăng ký nhà hàng, quản lý thực đơn (danh mục + món ăn), xem và cập nhật trạng thái đơn hàng.
- **Super Admin**: Quản lý toàn bộ hệ thống, duyệt/khoá nhà hàng, giám sát người dùng.

Dự án được xây dựng theo kiến trúc **Layered Architecture** (Controller → Service → Repository) chuẩn Spring Boot, sử dụng **Spring Security 6** với **JWT stateless authentication**, và tích hợp **Stripe** cho thanh toán trực tuyến.

---

## ✨ Tính năng chính

### 🔐 Xác thực & Phân quyền
- **Đăng ký / Đăng nhập** với email và mật khẩu
- **JWT Stateless Authentication** — token được tạo và xác thực không cần session server
- **JWT Access Token** gắn vào mọi request qua `Authorization: Bearer <token>`
- **Role-Based Access Control (RBAC)** với 3 vai trò: `ROLE_CUSTOMER`, `ROLE_RESTAURANT_OWNER`, `ROLE_ADMIN`
- Bảo vệ API endpoint theo từng role với Spring Security filter chain
- Custom `JwtTokenProvider` và `UserDetailsService` tích hợp Spring Security
- CORS configuration cho phép frontend React kết nối

### 👤 Quản lý Người dùng & Hồ sơ
- Xem và cập nhật thông tin hồ sơ cá nhân (tên, email, ảnh đại diện)
- Quản lý nhiều **địa chỉ giao hàng** (thêm, sửa, xoá, chọn địa chỉ mặc định)
- Upload ảnh đại diện (tích hợp Cloudinary hoặc lưu URL)
- Xem lịch sử đơn hàng của bản thân

### 🏪 Quản lý Nhà hàng
- **CRUD Nhà hàng**: Chủ nhà hàng tạo và quản lý hồ sơ nhà hàng của mình
- Thông tin nhà hàng: tên, mô tả, địa chỉ, loại ẩm thực (`cuisineType`), giờ mở cửa
- Trạng thái nhà hàng: `OPEN` / `CLOSED` — chủ nhà hàng tự toggle
- Danh sách nhà hàng công khai cho khách hàng duyệt và tìm kiếm
- **Admin** có thể duyệt, khoá hoặc xoá nhà hàng

### 🍽️ Quản lý Thực đơn
- **CRUD Danh mục món ăn** (`FoodCategory`): Chủ nhà hàng quản lý danh mục
- **CRUD Món ăn** (`Food`): Thêm, sửa, xoá món ăn với đầy đủ thông tin:
  - Tên, mô tả, giá, ảnh, danh mục, nhà hàng
  - Trạng thái: `available` / `not available`
  - Tuỳ chọn món (size, topping) — `IngredientsCategory` và `IngredientsItem`
- Lọc món ăn theo danh mục, trạng thái available
- Tìm kiếm món ăn theo tên, nhà hàng

### 🛒 Giỏ hàng & Đặt hàng
- **Cart Management**: Thêm, cập nhật số lượng, xoá món trong giỏ hàng
- Giỏ hàng gắn với từng user, persist trong database
- **Tạo đơn hàng** từ giỏ hàng với địa chỉ giao hàng được chọn
- **Vòng đời đơn hàng** (Order Lifecycle) hoàn chỉnh:

```
PENDING → CONFIRMED → PREPARING → OUT_FOR_DELIVERY → DELIVERED
                                                    ↘ CANCELLED
```

- Chủ nhà hàng cập nhật trạng thái đơn hàng (`/api/admin/order/:id/:status`)
- Khách hàng xem danh sách và chi tiết đơn hàng của mình

### 💳 Thanh toán Online với Stripe
- Tích hợp **Stripe Payment Gateway** (`stripe-java` v20.107.0)
- Tạo **Payment Intent** / **Checkout Session** khi khách đặt hàng
- Xử lý kết quả thanh toán thành công / thất bại
- Lưu trạng thái thanh toán vào đơn hàng (`PENDING`, `COMPLETED`, `FAILED`)

### 🔧 Tính năng Kỹ thuật
- **Global Exception Handling** — `@RestControllerAdvice` xử lý lỗi tập trung, trả về response JSON chuẩn
- **Bean Validation** — `@Valid` + Spring Validation trên DTO
- **Lombok** — giảm boilerplate code (getter, setter, constructor, builder)
- **Spring DevTools** — Hot reload trong môi trường development
- **CORS** — cấu hình cho phép request từ frontend React

---

## 🛠️ Tech Stack

### Backend Core
| Công nghệ | Phiên bản | Mục đích |
|---|---|---|
| Java | 17 (LTS) | Ngôn ngữ lập trình |
| Spring Boot | 3.2.4 | Application framework |
| Spring Web | 6.x | REST API (MVC) |
| Spring Data JPA | 3.x | ORM & Database access |
| Spring Security | 6.x | Authentication & Authorization |
| Hibernate | 6.x | JPA implementation |

### Database
| Công nghệ | Mục đích |
|---|---|
| MySQL 8.0 | Cơ sở dữ liệu quan hệ chính |
| Spring Data JPA | Repository pattern, JPQL queries |

### Security & Authentication
| Thư viện | Phiên bản | Mục đích |
|---|---|---|
| JJWT API | 0.11.5 | JWT token generation |
| JJWT Impl | 0.11.5 | JWT signing & verification |
| JJWT Jackson | 0.11.5 | JSON serialization cho JWT |
| Spring Security | 6.x | Filter chain, RBAC |

### Payment Integration
| Thư viện | Phiên bản | Mục đích |
|---|---|---|
| Stripe Java SDK | 20.107.0 | Thanh toán trực tuyến |

### Build & Developer Tools
| Công nghệ | Mục đích |
|---|---|
| Maven | Build tool & dependency management |
| Lombok | Giảm boilerplate (getter/setter/constructor) |
| Spring DevTools | Auto-restart khi develop |
| Spring Boot Test | Unit & Integration testing |
| Spring Security Test | Security layer testing |

---

## 🏛️ Kiến trúc hệ thống

```
┌─────────────────────────────────────────────────────┐
│              React Frontend (foodorderingui)         │
│         http://localhost:3000                        │
└──────────────────────┬──────────────────────────────┘
                       │ HTTP Request + JWT Bearer Token
                       ↓
┌─────────────────────────────────────────────────────┐
│          Spring Boot Backend (port 8080)            │
│                                                     │
│  ┌─────────────────────────────────────────────┐   │
│  │         Spring Security Filter Chain        │   │
│  │  JwtAuthenticationFilter → validate token   │   │
│  │  → inject SecurityContext (user + roles)    │   │
│  └──────────────────────┬──────────────────────┘   │
│                         ↓                           │
│  ┌──────────────────────────────────────────────┐  │
│  │              REST Controllers                 │  │
│  │  /auth/**  /api/**  /api/admin/**            │  │
│  └──────────────────────┬───────────────────────┘  │
│                         ↓                           │
│  ┌──────────────────────────────────────────────┐  │
│  │              Service Layer                   │  │
│  │  Business logic, validation, orchestration   │  │
│  └──────────────────────┬───────────────────────┘  │
│                         ↓                           │
│  ┌──────────────────────────────────────────────┐  │
│  │           Repository Layer (JPA)             │  │
│  │  Spring Data JPA Repositories                │  │
│  └──────────────────────┬───────────────────────┘  │
│                         ↓                           │
│  ┌──────────────────────────────────────────────┐  │
│  │        MySQL Database (port 3306)            │  │
│  └──────────────────────────────────────────────┘  │
│                                                     │
│  ┌──────────────────────────────────────────────┐  │
│  │          Stripe Payment Gateway              │  │
│  │  (External API — payment processing)         │  │
│  └──────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────┘
```

---

## 📁 Cấu trúc dự án

```
FoodOrderingWebBE/
├── src/
│   └── main/
│       ├── java/org/sang/FoodOrderingWeb/
│       │   │
│       │   ├── config/
│       │   │   ├── SecurityConfig.java        # Spring Security filter chain, CORS, CSRF
│       │   │   ├── JwtProvider.java           # JWT token generation & validation
│       │   │   └── AppConfig.java             # Bean configurations, PasswordEncoder
│       │   │
│       │   ├── controller/
│       │   │   ├── AuthController.java        # POST /auth/signup, /auth/signin
│       │   │   ├── UserController.java        # GET/PUT /api/users/profile, address
│       │   │   ├── RestaurantController.java  # GET /api/restaurants (public)
│       │   │   ├── FoodController.java        # GET /api/food (public menu)
│       │   │   ├── CartController.java        # POST/PUT/DELETE /api/cart
│       │   │   ├── OrderController.java       # POST/GET /api/order
│       │   │   ├── PaymentController.java     # POST /api/payment (Stripe)
│       │   │   └── AdminRestaurantController.java  # /api/admin/restaurant/**
│       │   │   └── AdminOrderController.java       # /api/admin/order/**
│       │   │   └── AdminFoodController.java         # /api/admin/food/**
│       │   │
│       │   ├── service/
│       │   │   ├── UserService.java / UserServiceImpl.java
│       │   │   ├── RestaurantService.java / RestaurantServiceImpl.java
│       │   │   ├── FoodService.java / FoodServiceImpl.java
│       │   │   ├── CartService.java / CartServiceImpl.java
│       │   │   ├── OrderService.java / OrderServiceImpl.java
│       │   │   └── PaymentService.java / PaymentServiceImpl.java
│       │   │
│       │   ├── repository/
│       │   │   ├── UserRepository.java
│       │   │   ├── RestaurantRepository.java
│       │   │   ├── FoodRepository.java
│       │   │   ├── FoodCategoryRepository.java
│       │   │   ├── CartRepository.java
│       │   │   ├── CartItemRepository.java
│       │   │   ├── OrderRepository.java
│       │   │   ├── OrderItemRepository.java
│       │   │   └── AddressRepository.java
│       │   │
│       │   ├── model/                         # JPA Entities
│       │   │   ├── User.java
│       │   │   ├── Restaurant.java
│       │   │   ├── Food.java
│       │   │   ├── FoodCategory.java
│       │   │   ├── IngredientsCategory.java
│       │   │   ├── IngredientsItem.java
│       │   │   ├── Cart.java
│       │   │   ├── CartItem.java
│       │   │   ├── Order.java
│       │   │   ├── OrderItem.java
│       │   │   ├── Address.java
│       │   │   └── Payment.java
│       │   │
│       │   ├── request/                       # Request DTOs
│       │   │   ├── LoginRequest.java
│       │   │   ├── CreateRestaurantRequest.java
│       │   │   ├── CreateFoodRequest.java
│       │   │   └── AddCartItemRequest.java
│       │   │
│       │   ├── response/                      # Response DTOs
│       │   │   ├── AuthResponse.java          # JWT token + role
│       │   │   ├── MessageResponse.java
│       │   │   └── PaymentResponse.java
│       │   │
│       │   ├── exception/                     # Custom exceptions & handler
│       │   │   └── GlobalExceptionHandler.java
│       │   │
│       │   └── FoodOrderingWebApplication.java  # Main class
│       │
│       └── resources/
│           └── application.properties           # DB, JWT, Stripe config
│
├── .mvn/wrapper/                # Maven wrapper
├── mvnw / mvnw.cmd              # Maven wrapper scripts
├── pom.xml                      # Dependencies
└── README.md
```

---

## 🔌 API Endpoints

### 🔓 Authentication (Public)

| Method | Endpoint | Mô tả |
|---|---|---|
| `POST` | `/auth/signup` | Đăng ký tài khoản mới |
| `POST` | `/auth/signin` | Đăng nhập, nhận JWT token |

**Request body — Đăng ký:**
```json
{
  "fullName": "Nguyen Van A",
  "email": "user@example.com",
  "password": "password123",
  "role": "ROLE_CUSTOMER"
}
```

**Response — Đăng nhập:**
```json
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9...",
  "message": "Login successful",
  "role": "ROLE_CUSTOMER"
}
```

---

### 👤 User (Authenticated — Any Role)

| Method | Endpoint | Mô tả |
|---|---|---|
| `GET` | `/api/users/profile` | Lấy thông tin hồ sơ user hiện tại |
| `PUT` | `/api/users/profile` | Cập nhật thông tin hồ sơ |
| `GET` | `/api/users/addresses` | Danh sách địa chỉ giao hàng |
| `POST` | `/api/users/addresses` | Thêm địa chỉ mới |
| `DELETE` | `/api/users/addresses/{id}` | Xoá địa chỉ |

---

### 🏪 Restaurant (Public)

| Method | Endpoint | Mô tả |
|---|---|---|
| `GET` | `/api/restaurants` | Danh sách tất cả nhà hàng |
| `GET` | `/api/restaurants/{id}` | Chi tiết một nhà hàng |
| `GET` | `/api/restaurants/search` | Tìm kiếm nhà hàng theo tên/keyword |
| `GET` | `/api/restaurants/{id}/food` | Menu (món ăn) của nhà hàng |

---

### 🍽️ Food (Public)

| Method | Endpoint | Mô tả |
|---|---|---|
| `GET` | `/api/food/search` | Tìm kiếm món ăn theo tên |
| `GET` | `/api/food/restaurant/{restaurantId}` | Lấy danh sách món theo nhà hàng |
| `GET` | `/api/food/restaurant/{restaurantId}/filter` | Lọc theo danh mục, vegetarian... |

---

### 🛒 Cart (ROLE_CUSTOMER)

| Method | Endpoint | Mô tả |
|---|---|---|
| `GET` | `/api/cart` | Lấy giỏ hàng hiện tại của user |
| `PUT` | `/api/cart/add` | Thêm món vào giỏ hàng |
| `PUT` | `/api/cart-item/{id}/quantity` | Cập nhật số lượng một item |
| `DELETE` | `/api/cart-item/{id}/remove` | Xoá một item khỏi giỏ |
| `PUT` | `/api/cart/clear` | Xoá toàn bộ giỏ hàng |

---

### 📦 Order (ROLE_CUSTOMER)

| Method | Endpoint | Mô tả |
|---|---|---|
| `POST` | `/api/order` | Tạo đơn hàng mới từ giỏ hàng |
| `GET` | `/api/order/user` | Danh sách đơn hàng của user |

---

### 💳 Payment (ROLE_CUSTOMER)

| Method | Endpoint | Mô tả |
|---|---|---|
| `POST` | `/api/payment` | Tạo Stripe Payment Session cho đơn hàng |

---

### 🔑 Admin — Restaurant Management (ROLE_RESTAURANT_OWNER)

| Method | Endpoint | Mô tả |
|---|---|---|
| `POST` | `/api/admin/restaurant` | Tạo nhà hàng mới |
| `PUT` | `/api/admin/restaurant/{id}` | Cập nhật thông tin nhà hàng |
| `DELETE` | `/api/admin/restaurant/{id}` | Xoá nhà hàng |
| `GET` | `/api/admin/restaurant` | Lấy nhà hàng của chủ đang đăng nhập |
| `PUT` | `/api/admin/restaurant/{id}/status` | Toggle OPEN/CLOSED |

---

### 🔑 Admin — Food & Category Management (ROLE_RESTAURANT_OWNER)

| Method | Endpoint | Mô tả |
|---|---|---|
| `POST` | `/api/admin/food` | Thêm món ăn mới |
| `DELETE` | `/api/admin/food/{id}` | Xoá món ăn |
| `PUT` | `/api/admin/food/{id}` | Cập nhật thông tin món ăn |
| `PUT` | `/api/admin/food/{id}/status` | Toggle available/unavailable |
| `POST` | `/api/admin/category` | Tạo danh mục mới |
| `GET` | `/api/admin/category/restaurant/{id}` | Lấy danh mục của nhà hàng |

---

### 🔑 Admin — Order Management (ROLE_RESTAURANT_OWNER)

| Method | Endpoint | Mô tả |
|---|---|---|
| `GET` | `/api/admin/order/restaurant/{id}` | Tất cả đơn hàng của nhà hàng |
| `PUT` | `/api/admin/order/{id}/{orderStatus}` | Cập nhật trạng thái đơn hàng |

---

## 🔐 Luồng xác thực JWT

```
1. Client gửi POST /auth/signin { email, password }
        ↓
2. Spring Security → AuthenticationManager.authenticate()
        ↓
3. UserDetailsService.loadUserByUsername(email)
        ↓ Lấy user từ DB, kiểm tra password với BCrypt
4. JwtProvider.generateToken(authentication)
        ↓ Ký token với SECRET_KEY (HS512), đặt expiration
5. Trả về { jwt: "eyJ...", role: "ROLE_CUSTOMER" }

─────────────────────────────────────────────────

Mỗi request sau đó:

1. Client gửi request với header:
   Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
        ↓
2. JwtAuthenticationFilter.doFilterInternal()
        ↓ Trích xuất token từ header
3. JwtProvider.validateToken(token)
        ↓ Kiểm tra chữ ký, expiry, format
4. JwtProvider.getEmailFromToken(token)
        ↓ Lấy email từ claims
5. UserDetailsService.loadUserByUsername(email)
        ↓ Tạo UsernamePasswordAuthenticationToken
6. SecurityContextHolder.setAuthentication(...)
        ↓ Request được xử lý với user context đầy đủ
7. Controller nhận @AuthenticationPrincipal User
```

---

## 💳 Luồng đặt hàng & thanh toán

```
Customer                   Backend                    Stripe
   │                          │                          │
   │─── POST /api/order ──────→                          │
   │        (cartId, addressId)│                          │
   │                          │ Tạo Order từ Cart        │
   │                          │ Trạng thái: PENDING      │
   │←── { orderId, total } ───│                          │
   │                          │                          │
   │─── POST /api/payment ────→                          │
   │        (orderId)          │                          │
   │                          │── Create PaymentIntent ──→
   │                          │                          │
   │                          │←── { clientSecret } ─────│
   │←── { paymentUrl } ───────│                          │
   │                          │                          │
   │── Redirect to Stripe ────────────────────────────→  │
   │   (Checkout page)         │                 Customer pays
   │                          │                          │
   │←── Stripe Webhook ───────────────────────────────── │
   │     payment_intent.succeeded                        │
   │                          │ Cập nhật Order:          │
   │                          │ paymentStatus = COMPLETED │
   │                          │ orderStatus = CONFIRMED  │
   │                          │                          │
```

---

## 🗄️ Thiết kế Database

### Các Entity chính và quan hệ

```
User (1) ──────── (1) Cart
 │                     │
 │                     │ (1-N) CartItem ── (N-1) Food
 │
 │ (1-N) Order ── (1-N) OrderItem ── (N-1) Food
 │            │
 │            └── (N-1) Address
 │
 └── (1-1) Restaurant (nếu là RESTAURANT_OWNER)

Restaurant (1) ── (1-N) FoodCategory
           (1) ── (1-N) Food
           (1) ── (1-N) IngredientsCategory ── (1-N) IngredientsItem

Food (N-1) ── FoodCategory
Food (N-N) ── IngredientsItem (tuỳ chọn món)

Order (1) ── (1) Payment
```

### Các Entity quan trọng

| Entity | Mô tả | Trường chính |
|---|---|---|
| `User` | Tài khoản người dùng | `id`, `email`, `password`, `role`, `fullName` |
| `Restaurant` | Nhà hàng | `id`, `name`, `cuisineType`, `openingHours`, `open`, `owner` |
| `Food` | Món ăn | `id`, `name`, `price`, `description`, `images`, `available`, `category` |
| `FoodCategory` | Danh mục món | `id`, `name`, `restaurant` |
| `Cart` | Giỏ hàng | `id`, `customer`, `total`, `items` |
| `CartItem` | Item trong giỏ | `id`, `food`, `quantity`, `totalPrice` |
| `Order` | Đơn hàng | `id`, `customer`, `restaurant`, `totalAmount`, `orderStatus`, `deliveryAddress` |
| `OrderItem` | Item trong đơn | `id`, `food`, `quantity`, `totalPrice` |
| `Address` | Địa chỉ giao hàng | `id`, `streetAddress`, `city`, `country`, `postalCode` |
| `Payment` | Thông tin thanh toán | `id`, `order`, `paymentMethod`, `paymentStatus`, `stripePaymentId` |

---

## 💻 Yêu cầu hệ thống

| Công cụ | Phiên bản tối thiểu |
|---|---|
| Java (JDK) | 17+ (LTS) |
| Maven | 3.8+ |
| MySQL | 8.0+ |
| Git | 2.x+ |

> **Tích hợp thanh toán**: Cần tài khoản Stripe để lấy API key (`publishable key` và `secret key`).

---

## 🚀 Hướng dẫn cài đặt & chạy

### 1. Clone repository

```bash
git clone https://github.com/NgSang0127/FoodOrderingWebBE.git
cd FoodOrderingWebBE
```

### 2. Tạo MySQL Database

```sql
CREATE DATABASE food_ordering CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Cấu hình `application.properties`

Mở file `src/main/resources/application.properties` và điền thông tin:

```properties
# ===== Database =====
spring.datasource.url=jdbc:mysql://localhost:3306/food_ordering?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ===== JPA / Hibernate =====
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# ===== JWT =====
jwt.secret=your_very_long_and_secure_secret_key_minimum_512_bits
jwt.expiration=86400000

# ===== Stripe =====
stripe.api.key=sk_test_your_stripe_secret_key

# ===== CORS =====
frontend.url=http://localhost:3000

# ===== Server =====
server.port=8080
```

### 4. Build và chạy ứng dụng

```bash
# Build (bỏ qua test)
./mvnw clean package -DskipTests

# Chạy ứng dụng
./mvnw spring-boot:run
```

Hoặc chạy file JAR:

```bash
java -jar target/FoodOrderingWeb-0.0.1-SNAPSHOT.jar
```

Ứng dụng sẽ khởi động tại: **http://localhost:8080**

Spring Boot sẽ tự động tạo/cập nhật schema database nhờ `ddl-auto=update`.

### 5. Kiểm tra API

Dùng Postman hoặc curl:

```bash
# Đăng ký
curl -X POST http://localhost:8080/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test User","email":"test@example.com","password":"123456","role":"ROLE_CUSTOMER"}'

# Đăng nhập
curl -X POST http://localhost:8080/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"123456"}'

# Lấy danh sách nhà hàng (có JWT)
curl -X GET http://localhost:8080/api/restaurants \
  -H "Authorization: Bearer <your_jwt_token>"
```

---

## ⚙️ Cấu hình Application

### JWT Configuration

| Property | Mô tả | Giá trị mặc định |
|---|---|---|
| `jwt.secret` | Secret key để ký JWT (HMAC-SHA512) | *(bắt buộc cấu hình)* |
| `jwt.expiration` | Thời gian hết hạn token (ms) | `86400000` (24 giờ) |

> ⚠️ **Bảo mật**: Secret key phải đủ dài (tối thiểu 64 ký tự cho HS512) và tuyệt đối không commit lên Git. Sử dụng biến môi trường hoặc file `.env` cho production.

### Stripe Configuration

| Property | Mô tả |
|---|---|
| `stripe.api.key` | Secret key từ Stripe Dashboard (bắt đầu bằng `sk_test_` hoặc `sk_live_`) |

### Database Configuration

| Property | Mô tả |
|---|---|
| `spring.jpa.hibernate.ddl-auto` | `update` — tự động tạo/update schema; dùng `validate` cho production |
| `spring.jpa.show-sql` | `true` — hiển thị SQL queries (nên tắt ở production) |

---

## 🔐 Bảo mật & Best Practices

- **Password Hashing**: Mọi mật khẩu được hash bằng **BCryptPasswordEncoder** — không lưu plaintext
- **Stateless JWT**: Không dùng server-side session — dễ scale horizontal
- **Role-based endpoint protection**: `@PreAuthorize` và Security config bảo vệ từng endpoint theo role
- **Input Validation**: `@Valid` + Bean Validation trên mọi request DTO
- **CORS**: Chỉ cho phép origin từ frontend URL được cấu hình
- **Secret Management**: JWT secret và Stripe key nên lưu trong biến môi trường, không hard-code

---

## 🤝 Đóng góp

1. Fork repository
2. Tạo feature branch: `git checkout -b feature/ten-tinh-nang`
3. Commit: `git commit -m 'feat: thêm tính năng X'`
4. Push: `git push origin feature/ten-tinh-nang`
5. Tạo Pull Request

---

## 👨‍💻 Tác giả

**Nguyễn Công Sáng (NgSang0127)**

- GitHub: [@NgSang0127](https://github.com/NgSang0127)
- Email: nsang0127@gmail.com
- LinkedIn: [ngsang0127](https://www.linkedin.com/in/ngsang0127/)
---

<div align="center">

Made with ❤️ by **Nguyễn Công Sáng**

⭐ Nếu dự án này hữu ích, hãy cho một star để ủng hộ!

</div>
