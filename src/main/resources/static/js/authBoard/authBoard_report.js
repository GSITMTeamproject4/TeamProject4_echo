var win = navigator.platform.indexOf('Win') > -1;
if (win && document.querySelector('#sidenav-scrollbar')) {
    var options = { damping: '0.5' }
    Scrollbar.init(document.querySelector('#sidenav-scrollbar'), options);
}

function confirmReport(event) {
    event.preventDefault();
    const radios = document.getElementsByName('reason');
    let isChecked = false;
    for (let i = 0; i < radios.length; i++) {
        if (radios[i].checked) {
            isChecked = true;
            break;
        }
    }
    if (!isChecked) {
        alert('신고 사유를 선택해주세요.');
        return;
    }
    if (confirm('정말 신고하시겠습니까?')) {
        event.target.submit();
    }
}

document.getElementById("cancelButton").addEventListener("click", function() {
    var url = this.getAttribute("data-url");
    window.location.href = url;
});