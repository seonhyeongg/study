const output = document.getElementById("output")
const log = (msg) => (output.innerText += msg + "\n") // 문자열을 전달받아 output.innerText에 한 줄씩 추가

// 비동기 함수 (Promise)
// 사용자 정보 가져오기
/*
1. fetchUser는 Promise를 반환함
2. setTimeout을 통해 1초 후에 resolve 실행
   -> id, name, age 정보를 담은 객체 반환 (API 호출 역할을 대신함)
*/
function fetchUser(id) {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve({id, name: "Steve", age: 25})
        }, 1000)
    })
}

// 사용자 게시글 가져오기
/*
1. fetchPosts는 Promise를 반환함
2. setTimeout을 통해 1초 후에 resolve 실행
   -> 특정 사용자의 게시글 배열 반환 (API 호출 역할을 대신함)
*/
function fetchPosts(userId) {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve([
                { id: 1, title: "1st Post" },
                { id: 2, title: "2nd Post" },
            ])
        }, 1000)
    })
}

// async 함수 - 항상 Promise를 반환함
// 사용자 정보 및 게시글 로드
async function loadUserData() {
    output.innerText = ""; // 출력 영역 초기화
    log("Loading User Info...")

    // try...catch 블록으로 예외 처리
    try {
        // fetchUser(1) 호출 -> 1초 후 사용자 객체 봔환
        // await을 통해 Promise가 해결될 때까지 함수 실행 일시 중단
        const user = await fetchUser(1);
        log(`User Name: ${user.name}, Age: ${user.age}`) // 전달받은 사용자 정보 출력

        log("Loading Post...")

         // fetchPosts(user.id) 호출 -> 1초 후 게시글 배열 반환
        const posts = await fetchPosts(user.id)

        log("User Post:");
        // forEach로 게시글 배열을 순회하며 한 줄씩 출력
        posts.forEach((post) => {
            log(`- ${post.title}`)
        })
    } catch (error) {
        log(`Error: ${error}`)
    }

    log(`Task End`) // 오류 여부와 무관하게 출력
}