## SmogCheck
미세먼지 API를 가져와 간단히 표시하는 Android 앱입니다.

### 사용한 기술
- Jetpack Compose  
안드로이드 UI
- ktor  
대기오염정보 Json Api 요청
- coil  
환경 정보 정적 이미지 불러오기
- vico  
그래프 그리기
- opencsv  
CSV 파일 불러오기

### 사용한 api
- [한국환경공단_에어코리아_대기오염정보](https://www.data.go.kr/data/15073861/openapi.do)
- [에어코리아 | 측정소 정보](https://airkorea.or.kr/web/stationInfo?pMENU_NO=93)

### 만들면서 배운 것들 정리
vico 라이브러리의 Chart를 써 보긴 했는데 어떻게 꾸며야 하는지 잘 모르겠다.
index 표시될 공간을 없애고 그래프를 꽉차게 그리고 싶은데 관련 가이드가 없다.

context가 뭔지 제대로 알아야 할 것 같다.
이 프로젝트 안에서는 context를 csv 파일 불러오는 용으로만 써서 이해가 잘 안 된다.  
공식 문서에서는 전역 정보를 제공하는 인터페이스라는데, 그럼 이게 단가? 하드코딩된 텍스트들을 옮기는 데 쓸 수 있다는 건 알겠다.

DropDownMenu를 처음 써 봤다. 
expanded 인수를 받는데, expanded가 true일 때만 보이고 false면 아예 보이지 않는다.
Button을 따로 만들어서 해결하긴 했는데, 이게 맞는건지...

json class 생성 plugin도 처음 써 봤는데, 복잡한 json이 나오면 subclass 파일을 더 만든다.
이때 subclass의 이름은 json에서 가져온다.
당연히 굉장히 일반적인 이름으로 만들어지고, 꽤 높은 확률로 고장날 듯. 폴더를 하나 만들어서 거기에서 써야겠다.  
또 분석 중에 데이터가 전부 null이었으면 타입을 Any로 표시하는데, 이거는 Serializable이 안 붙는다. 처음에 당황했었다.

인공지능에게 'border의 위쪽에만 선을 그릴 방법'을 물어봤더니 Modifier.drawWithContent를 추천했다. 
drawLine()으로 직접 그리라는 건데, 좀 많이 신박했다.

AsyncImage 너무 편하다. _이렇게 쉬워도 되는 걸까요?_

현재 시간 문자열을 가공하기 위해 DateTimeformatter를 사용했다.
전체 시간 형식으로 parse 하고 부분 시간 형식에 format을 사용해서 문자로 만든다.

반복문 안에 fillMaxSize() 넣으면 안 된다.
남은 공간에서 값만큼 가져가는 식이라서, 남는 칸이 곱연산이 된다. 요소가 여섯 개라서 fillMaxSize(1f / 6)을 써 놓으니까 0.84 ^ 6 = 0.35만큼의 빈 칸이 생긴다.

### 스터디에서 배운 내용
맞다 let에서 it있었지

```kotlin
produceState(0) {

value = lat to lng  // == Pair("A", "B")
}
```
타이머는 produceState로 만드는 것이 좋다.

jetpack glance  
알파단계임 ㅎㅎ;;
하지만 생각보다 쉽다고.

Philip Lackner - How to build a home screen widget in Jetpack compose with Glance

Recomposition이 돌아가지 않는다.  
Thread를 상속받기도 불편하다.

그래서 만들어진 CoroutineScope(Dispatchers.*).lauhch {}  
- main은 UI에서
- default는 복잡한 계산에서
- IO는 네트워크 / 가져오기. 이도 저도 아니니까 IO를 썼던 느낌
각 작업에 최적화되어 있다.

launch는 일종의 스레드 구분 기능이기도 하다.
```kotlin
launch {

}
launch {

}
```
동시에 실행이 되지만 뭐가 먼저 될지는 아무도 모름  
job.cancel() 하면 멈춘다.

여기는 비동기 처리를 값 바꾸는 걸 감지하고 UI를 다시 로드하는 식으로 하는구나..

mutex, semaphore 

a = async {} 선언: 지금 실행 시작 deffered  
a.await() 호출: 결과 기다리기  
js의 그거랑 비슷하긴 하다.

measureTime {} : 시간 측정!