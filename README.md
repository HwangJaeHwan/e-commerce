# 프로젝트 구조
![msa 구성도 예시](https://github.com/user-attachments/assets/448c8e92-a30b-42ca-80b5-9e379c7777bd)
# 기술 스택
Vue.js, TypeScript, Spring Boot 3.3, Spring Data JPA, Spring Cloud Gateway, Spring Cloud Config, H2, Querydsl, Kafka, Rabbitmq

# 서비스 기능

* user-service
  * 회원가입, 로그인 등 유저와 관련된 서비스
  * 유저의 배송지에 관련된 서비스

* item-service
  * 상품 등록, 수정, 삭제 등 상품과 관련된 서비스

* order-service
  * 주문 및 장바구니를 관리하는 서비스

* review-service
  * 리뷰 등록, 수정, 삭제 등 리뷰와 관련된 서비스

* image-service
  * 이미지 등록, 수정 삭제 등 이미지와 관련된 서비스

* pay-service
  * 결제의 검증 및 취소 등 결제와 관련된 서비스
# 문제 해결


* JWT 처리 로직의 중앙 집중화 및 코드 중복 최소화
  * JWT 처리 로직을 API Gateway로 통합해, API Gateway에서 인증을 중앙 집중화하여 관리하게 하였습니다. 각 마이크로서비스에서 발생한 코드 중복을 최소화하고 유지보수를 용이하게 하며, 인증 로직의 일관성을 보장하여 전체 시스템의 보안을 강화했습니다.
* 구성 정보 변경 시 서비스 재시작 문제
  * 기존에는 구성 정보 변경 시 각 서비스를 재시작해야 했습니다. 이를 해결하기 위해 Spring Config Server를 도입했습니다.
* 구성 정보 변경 시 refresh 요청 문제
  * 구성 정보를 변경할 때마다 개별적으로 refresh 요청을 해야 하는 비효율적인 문제가 있었습니다. 이를 해결하기 위해 RabbitMQ와 Spring Cloud Bus를 도입하여 변경 사항을 실시간으로 전파하고 설정을 최신 상태로 유지했습니다.
* 상품 수량 동기화를 위한 Kafka 사용
  * 기존에는 상품의 수량 동기화를 위해 Feign Client를 사용했습니다. 하지만 이 방식은 서비스 간의 강한 결합을 초래하고, 동기화 과정에서의 성능 문제와 지연이 발생할 수 있었습니다. 이를 해결하기 위해 Kafka를 도입하여 비동기 방식으로 동기화를 처리했습니다. 이를 통해 서비스의 확장성과 가용성을 크게 향상시킬 수 있었습니다. 
  
