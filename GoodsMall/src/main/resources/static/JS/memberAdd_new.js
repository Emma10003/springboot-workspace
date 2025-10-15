/*
memberAdd.js 와 기능은 비슷한데 처음 보는 구조!
프로젝트를 할 때는 memberAdd.js 와 memberAdd_new.js 두 구조 중 한 가지만 쓰는 게 바람직하다
(둘 다 혼용해서 사용할 경우 AI로 아무거나 가져와서 붙였다고 생각할 수 있음)
 */
async function submitMember(event) {
    event.preventDefault();

    const alertBox = document.getElementById('alertBox');
    const form = document.getElementById('memberForm');

    const formData = new FormData(form);

    const memberData = {};
    formData.forEach((value, key) => {
        memberData[key] = value;
    });

    try {
        const response = await fetch('/api/member/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(memberData),
        });

        if (response.ok) {
            alertBox.innerHTML = `<div class="alert success">회원가입이 성공적으로 완료되었습니다.</div>`;
            form.reset();
        } else {
            const errorText = await response.text();
            throw new Error(errorText || '회원가입 중 오류가 발생했습니다.');
        }
    } catch (error) {
        alertBox.innerHTML = `<div class="alert error">오류: ${error.message}</div>`;
        console.error('Fetch error:', error);
    }
}