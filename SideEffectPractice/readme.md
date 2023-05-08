## SideEffectPractice
side-effect 함수를 이것저것 써보려고 만든 프로젝트입니다. lorem ipsum을 여러 번 출력합니다.
사용한 lorem ipsum은 다음과 같습니다: 
```Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.``` 
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
  snapshot이 뭔가요?
