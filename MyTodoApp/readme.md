## MyTodoApp
간단한 위젯이 있는 Android To-Do 앱입니다.

### 사용한 기술
- Jetpack Compose  
안드로이드 앱 UI
- Jetpack Glance  
안드로이드 위젯 UI
- Jetpack DataStore  
데이터 로컬 저장
- NSV (Newline Separated Value)  
방금 내가 지어낸 기술인데, 좀 멋진 것 같음  
데이터들을 \n (newline)으로 분리함!

### 만듦면서 배운 것들 정리
Glance는 1.0.0-alpha05버전을 쓰는 것이 좋다.
애초에 예제가 그것밖에 없다.  
1.0.0-beta01 버전을 쓰는 게 좋다고는 하는데 함수 정의부터 다 바뀌어서 쓰기 힘듬  
구글 공식에서 보여주는 저장소는 짤리고 maven은 구버전이고 난리가 났다.

모든 Layout과 Modifier가 비슷하게 생긴 Glance용으로 다시 만들어져 있다. 
= 코드 호환성 X.  
하지만 비슷하게 보이긴 하니 대충 OK.

[Row / Column에서 Arrangement를 못 쓴다.](https://issuetracker.google.com/issues/282129986)  
양 쪽에 모두 가로/세로 Alignment가 있는데 이거 의도된 건가?  
나는 간단한 UI만 만들어서 Box에 각각 Box를 만들고 Alignment를 설정해서 해결했는데, 복잡한 UI는 못 만들 것 같다. 애초에 복잡한 UI는 Widget에서 만들면 안 되기도 하고.

Button에 사진을 못 넣는다. Glance의 버튼은 무조건 string text / ActionCallback onClick을 받게 고정되어 있다.
뭐 Widget이니까.

Button에 의해 Widget을 수정하는 게 까다롭다.  
- 우선 PreferencesKey를 만들어야 한다.
이거는 Content의 currentState에서 꺼내 쓸 수 있다.  
- 그 다음, ActionCallback을 상속받은 Class를 만들고, onAction을 재정의한다.
버튼을 눌렀을 때의 행동이 된다.  
- 이 행동 안에서, updateAppWidgetState를 호출하고 그 안에서 widget.update를 하면 업데이트가 된다.  
- 바꿔야 될 값이 있다면 it을 사용해서, 혹은 적당한 이름을 붙이고  
`prefs[widget.data] = `하고 수정하면 된다.  
- 이렇게 만든 클래스는 onClick에 actionRunCallback 함수에, class를 인수로 주거나 (영상에서) Generic을 써서 (내가 짠 코드) 사용할 수 있다.

Side-effect가 모든 데이터 변화를 감지하는 건 아니다 - 
생각해보니 내 구현은 외부 데이터를 복사해놓은 내부 데이터의 변화만 감지하고 있었다. 이러니 감지가 안 되지.  
그렇다고 외부 데이터를 검사할 순 없다! 그건 단순히 생각해봐도 I/O 비용이 들 것.

DataStore는 기본적으로 flow 예제만 소개되어 있다. _그리고 온갖 블로그들에서 flow를 UI 함수 안에 합쳐놓은 예제만 어딘가에서 복붙해 두었다._  
이 사람들의 목표가 대체 뭘까? 비즈니스 로직을 UI 사이에 최대한 꽁꽁 숨기는 것?  
나는 차라리 동기식 처리를 하는 걸 선택했다.

_데이터 새로고침을 어느 시점에서 할 지를 선택하는 것은 아주 중요하다._  
아마 그걸 선택할 수 있는 사람은 구글에서 굉장히 높은 자리에 올랐을 것이다.  
Compose에는 onResume이 없어서, 위젯에서 값을 수정하고 돌아왔을 때를 담당하는 타이밍을 정하지 못했다. stackOverflow에서는 어떤 편법을 쓰면 가능하다고 한다...  
Glance에는 무슨 Action이 많아서 기능 자체는 있는 것 같은데, 나는 예제가 충분히 없으면 코딩을 못 한다!  

updateAppWidgetState에 버그가 있는 건지 내가 잘못한 건지, 데이터를 넣고 Widget 업데이트를 바로 하면 상태 반영이 안 된다.  
그래서 update를 두 번 하니까.. 작동했다. _대체 왜?_
```kotlin
updateAppWidgetState(context, glanceId) { prefs ->
    prefs[CounterWidget.jsonTodoList] = v
    CounterWidget.update(context, glanceId)
}
updateAppWidgetState(context, glanceId) {
    CounterWidget.update(context, glanceId)
}
```
### 총평
어느 순간부터 구글 앱들의 Widget 퀄리티가 심각하게 떨어졌었는데, 아마 얘 때문인 것 같다.  
정식 출시되고 보자. 내가 멍청해서 미안.


### 오늘 배운 내용 나머지
mvvm  
weight는 사랑입니다

companion object에 const val KEY_UID = "uid" 넣고
```kotlin
var uid: String? 
    get() = getString(GET_UID)
    set() = setString(GET_UID)    
}
```
이런 식으로.
getter / setter를 이런 곳에서 다시 보게 되니까 반갑다.

solid 원칙
없으면 취업못함  
개발에는 정답이 없지만 조금더 나은 코드냐는 있다

LocalStorage는 LocalStorageImpl에서만 상속받나요? 기능 수정하면 LocalStorageImpl2 만들면 되나요? => 칭찬스티커 받음, __야호!__  
Dependency Injection을 그냥 쓰는게 아니라 Koin나 Hilt으로 쓴다

검색하면 다 나옴. 따라만 하세요.

폴더 관리는 클린 아키텍처를 따라야 하는데.  
폴더 너무 많아...  
지금까지 개발하던 건 야매긴 한데......

DataSource를 만들어본 적이 없다.  
data 관련된 함수 몇 개를 다른 곳에서 관리하는 느낌.  
MockRepositoryImpl  
Repository 패턴 vs clean architecture.