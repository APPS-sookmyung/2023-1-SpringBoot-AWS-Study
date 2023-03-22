# WEEK1
## 섹션 1: 오리엔테이션
- 실제 상용화된 앱들은 어떤식으로 구현이 가능할까?
    - 기능들을 직접 구현
    - 다른 앱에 사용 가능
- ui와 데이터를 어떻게 넣을지 미리 고민해보기!
- Swift
    - 빠른
    - Objective-C를 대체
    - Optional를 활용한 Safe를 보장
        - 코드를 작성하는 동안에 오류 확인 가능
        - 런타임에서 발생한 에러를 컨파일 타임에서 확인 가능
    - 프로토콜 지향 → 객체간의 소통, 기능 확장 가능
    - 객체지향, 함수형의 특징을 가지고 있음
    - 타입추론을 이용한 변수선언
        - 변수의 타입을 선언해 주지 않아도 자동으로 해줌

***

<br>

## 섹션 2: 프로젝트 환경설정 및 실습
- Swift 문법
    1. 변수와 상수 선언
        
        - 상수
            
            ```swift
            let name: String = "Uno"
            let swift = "Swift"
            ```
            
        - 변수
            
            ```swift
            var year: Int = 2022
            var y = 2023
            
            year = 2023
            ```
            
    2. 함수 만들기
        
        ```swift
        func sum(a: Int, b: Int) -> Int {
            return a + b
        }
        
        print(sum(a:1,b:2))
        
        func multiply(a: Int, b:Int) -> Int{
            a * b
        }
        
        print(multiply(a: 10, b: 10))
        ```
        
    3. 이름 짓기

        - Lower Camel Case 

            - 인스턴스 / 메소드 / 함수

            - 시작은 소문자 나머지 단어의 시작은 대문자
            
            ```swift
            let viewController = UIViewController()
            ```
        - Upper Camel Case
            - 구조체 / 클래스 / 프로토콜
            - 시작과 나머지 단어도 모두 대문자
            
            ```swift
            struct Person{
                let a: Int
                let b: Int
            }
            
            class Operator{
                let a: Int
                let b: Int
                init(a:Int, b:Int){
                    self.a=a
                    self.b=b
                }
            }
            
            protocol Flyable{
                func fly()
            }
            ```
*** 
<br> 

## 섹션 3: 오토레이아웃
- 뷰의 크기와 위치를 설정하는 도구
- Constraint(제약조건)을 통해서 크기와 위치 결정
- 외부적, 내부적 요인에 의한 크기 변화에 동적으로 대응
- 세로 → 가로 적절하게 화면 배치 가능
- 사용자의 클릭에 반응하여 화면이 바뀌는 경우

### 실습
- 색상 : 인스펙터 창 → Attributes inspector(5번째) → background
- 뷰 추가 : 우측 상단 + → object library → UIView
- 뷰를 맨아래에 붙여도 다른 시뮬레이터로 실행시키면 위치가 달라짐 → Constraint 설정해줘야 함
    - Constraint 부여 : 뷰 클릭 → 우측 하단 3번째 → Add New Constraints
    - 기기를 바꿔가며 위치를 볼 수 있음
- AutoLayout : x, y축, 높이, 너비에 대한 정보를 정확하게 주거나 추론이 가능해야함
    - 너비를 주지 않아도 추론이 가능하면 됨 ex) 좌측과 0만큼 떨어지고 우측과 0만큼 떨어짐
- Align : 뷰 클릭 → 우측 하단 2번째 → Horizontally, Vertically 설정 가능
    - 위로 4/1 → Size inspector → x축 클릭 → edit → Multiplier 0.5 설정
    - 위, 아래, 양옆 동일한 방법으로 위치 설정 가능
- 경고 → x축, 너비 제약조건 필요 or y축, 높이 제약조건 필요 → 정확하게 주지 않았거나 추론 불가능
    - 너비를 비율로 주고 싶을 때 → command + 뷰 클릭하며 배경화면에 드롭 → Equal Widths(Heights)
- 부여한 Constraints 볼 때 → 인스펙터 창 → Size inspector(6번째) → Constraints → 원하는 막대 클릭 → ex) Proportional width to : Superview → edit → Multiplier 배수 설정     