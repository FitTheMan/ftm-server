## Git Commit Message Convention

### 기본구조

```
<Prefix> : <Subject>

<Body>

<Footer>
```

#### Prefix
|    타입    | 설명                                   |
|:--------:|--------------------------------------|
|   feat   | 새로운 기능 추가                            |
|   fix    | 버그 수정                                |
|   docs   | 문서 내용 변경                        |     
|   style  | 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 등  |        
| refactor | 코드 리팩토링                              |
|   test   | 테스트 코드 작성                            |
|   chore  | 빌드 수정, 패키지 매니저 설정, 운영 코드 변경이 없는 경우 등  |


#### Subject (제목)

`Subject(제목)`은 최대 50글자 이하로 마침표와 특수기호는 사용하지 않는다.

영문 표기 시, 첫글자는 대문자로 표기하며 과거시제를 사용하지 않는다.

<br>

#### Body (본문)
`Body (본문)`은 부연설명이 필요한 경우에 작성한다.(생략가능)

영문 기준 72글자 이하로 작성한다.

<br>

#### Footer (꼬리말)

`Footer (꼬리말)`은 이슈 트래커의 ID를 작성한다.

<br>


#### 커밋 메시지 예시

위 내용을 작성한 커밋 메시지 예시

```markdown
feat: Summarize changes in around 50 characters or less

More detailed explanatory text, if necessary. Wrap it to about 72
characters or so. In some contexts, the first line is treated as the
subject of the commit and the rest of the text as the body. The
blank line separating the summary from the body is critical (unless
you omit the body entirely); various tools like `log`, `shortlog`
and `rebase` can get confused if you run the two together.

Explain the problem that this commit is solving. Focus on why you
are making this change as opposed to how (the code explains that).
Are there side effects or other unintuitive consequences of this
change? Here's the place to explain them.

Further paragraphs come after blank lines.

 - Bullet points are okay, too

 - Typically a hyphen or asterisk is used for the bullet, preceded
   by a single space, with blank lines in between, but conventions
   vary here

If you use an issue tracker, put references to them at the bottom,
like this:

Resolves: #123
See also: #456, #789
```
<br>
