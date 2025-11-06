
const API_BASE_URL = "http://localhost:8080/api";   // 공통으로 들어가는 주소들 묶어놓기 -> 나중에 한 번에 변경하기 용이함!


async function fetchBoardData(){
    const loading = document.getElementById("loading");
    const error = document.getElementById("error");
    const table = document.getElementById("boardTable");
    const tbody = document.getElementById("boardBody");

    loading.style.display = "block";
    error.style.display = "none";
    table.style.display = "none";
    tbody.innerHTML = "";

    const res = await fetch(API_BASE_URL + "/board/all")

    // ok = 200, 200 이 아닌 게 맞을 때
    if(!res.ok) {
        throw new Error("서버 응답 오류 : " + res.status);
    }
    const board = await res.json();

    if(board.length === 0) { // 게시글이 하나도 존재하지 않을 때
        tbody.innerHTML = `<tr><td colspan="5">게시글이 없습니다.</td></tr>`;
    } else {
        board.forEach(
            b => {  // 여기서 b는 JSON에서 각각의 board 데이터를 받음
                const row = document.createElement("tr");   // 요소 만들기

                row.innerHTML = `
                    <td>${b.id}</td>
                    <td class="title-cell" onclick="openModal(${b.id})">${b.title}</td>
                    <td>${b.writer}</td>
                    <td>${b.viewCount}</td>
                    <td>${b.createdAt}</td>
                    <td onclick="openModal(${b.id})">미리보기</td>
                    <td onclick="gotoDetail(${b.id})">상세보기</td>
                `;
                tbody.appendChild(row); // tbody에 행 추가 (.appendChild?)
            });
    }

    table.style.display = "table";
}

async function detailFunction(id) {
    const res = await fetch(API_BASE_URL + `/board/${id}`)

    if(!res.ok) {
        throw new Error("게시글을 불러오는 데에 실패했습니다.");
    }
    return await res.json();
}

async function fetchBoardDetail() {
    const urlParams = new URLSearchParams(window.location.search);
    const boardId = urlParams.get("id");

    if(!boardId) {
        alert("잘못된 게시글 번호입니다.");
        window.location.href = "/board";
    }
    
    const b = await detailFunction(boardId);
    console.log("DB 데이터 조회 : ", b);
    
    const title = document.querySelector(".board-title");
    const writer = document.querySelector(".board-writer");
    const date = document.querySelector(".board-date");
    const views = document.querySelector(".board-views");
    const content = document.querySelector(".board-content");

    title.textContent = b.title;
    writer.textContent = b.writer;
    date.textContent = b.createdAt;
    views.textContent = b.viewCount;
    content.textContent = b.content;
}

async function openModal(id) {
    const modal = document.getElementById("viewModal");
    const modalTitle = document.getElementById("modalTitle");
    const modalInfo = document.getElementById("modalInfo");
    const modalContent = document.getElementById("modalContent");

    modal.style.display = "flex";

    const board = await detailFunction(id);

    modalTitle.textContent = board.title;
    modalInfo.textContent = `작성자 ${board.writer}`;
    modalContent.textContent = board.content;
}

function closeModal() {
    const modal = document.getElementById("viewModal");
    modal.style.display = "none";
}

window.addEventListener("DOMContentLoaded", () => {
    if(document.getElementById("boardTable")) {
        fetchBoardData();
    }

    if(document.querySelector(".board-detail-container")) {
        fetchBoardDetail();
    }
});

const listBtn = document.querySelector(".btn-list");
listBtn.addEventListener("click", gotoList);

function gotoDetail(id){
    window.location.href = `/board/detail?id=${id}`;
}

function gotoList() {
    window.location.href = `/board`
}