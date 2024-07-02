function confirmDelete(form) {
    if (confirm("정말로 삭제하시겠습니까?")) {
        form.submit();
    } else {
        return false;
    }
}