## SideEffectPractice
side-effect 함수를 이것저것 써보려고 만든 프로젝트입니다. lorem ipsum을 여러 번 출력합니다.
사용한 lorem ipsum은 다음과 같습니다: 
```
Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
```

[wikipedia](https://en.wikipedia.org/wiki/Lorem_ipsum)에서 가져왔습니다.

### 구성
하단에 좌우 이동 버튼이 있고, 중앙에서는 Int 변수 page에 따라 lorem ipsum이 출력되는 페이지 몇 개를 보여줍니다.

- 0: SamplePage  
  hello, world를 출력합니다.

- 1: DelayedIpsum  
  LaunchedEffect를 사용해 lorem ipsum을 500ms에 한 단어씩 출력합니다.

- 2: ResumeableIpsum  
  LaunchedEffect를 사용해 lorem ipsum을 출력하고, 다른 페이지로 넘어가서 출력을 중단했다면 DisposableEffect로 중단 지점을 내보냅니다.
  추가 인수를 사용해 중단 지점부터 시작할 수 있습니다.
  DisposableEffect는 LaunchedEffect와 다르게 coroutine이 아니어서 delay를 못 썼습니다.

- 3: ModifiableIpsum  
  LaunchedEffect를 사용해 lorem ipsum을 출력하고, 따로 추가한 버튼을 누르면 글자 색이 무작위로 변경됩니다.
  rememberUpdatedState로, 인수로 들어온 바뀐 글자 색을 가져옵니다.

- 4: DelayedIpsumbutRedWith  
  DelayedIpsum과 같은 기능을 하지만, 마지막 단어의 첫 글자가 e라면 글자 색이 빨간색이 됩니다.
  derivedStateOf를 사용하긴 했는데 안 쓸 때랑 차이를 정확히 모르겠습니다.

- 5: DelayedIpsumWithButtons  
  '+' 버튼을 누르면 lorem ipsum을 출력하고, 'x' 버튼을 누르면 중단합니다.
  rememberCoroutineState를 사용했습니다. 
  coroutine.launch를 두 번 쓸 수가 없는 것 같아서, 아쉽지만 누른 버튼을 비활성화했습니다.

- FrameCounter  
  SideEffect를 사용해서 recomposition이 몇 번 됐는지를 표시합니다.
  recomposition이 화면 내용이 확실히 바뀔 때만 될 줄 알았는데, 1초에 50번 넘게 불렸습니다. SideEffect에 중요한 거 넣으면 안 되겠다.

### 안 쓴 것들
  
- produceState  
  기능이 너무 복잡해서 이해를 못 했습니다.

- snapshotFlow  
  flow가 뭔지 몰라서, 그리고 flow를 써야 하는 기능을 아직 안 써서 안 썼습니다.

### 고쳐야 할 것
- sealed class 아니면 enum 사용하기
- 클린 아키텍처 찾아보기
- 너무 많은 중복 코드 없앨 방법 찾아보기

### 스터디 중 배운 내용
주제를 잘못 만들었다. 바꿔야 될 걸 바꾸는 내용이 아니라, 바꾸지 말아야 할 걸 바꾸지 말아야 하는 내용이다.  
(예: TextField가 바뀔 때마다 반응하는 대신에...)

AnimatedVisibility(visible = isShowToast) { } 하고 visible을 coroutine에서 바꾸면 된다.

onDispose에는 소멸자를 넣어야 한다.

kotlin의 coroutine은 cancel하면 다시 시작할 수 없다.

produceState에서 값이 바뀌면 coroutine cancel이 됨. 무한 반복도 멈춘다.

> composable 뒤에서는 잘 실행이 될 건데~  
rememberUpdatedState는 coroutine에서 쓰는 거네. 이것도 잘못 쓰고 있었네.

The Clean Architecture.
워터폴? 스크럼? 애자일?  
아키텍처는: 개발자 대상으로 진행되는 아키텍처. 재사용성. 업무 분할.  

SOLID 원칙.