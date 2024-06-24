$(document).ready(function() {
    var csrfToken = $('meta[name="_csrf"]').attr('content');
    var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    $('.heart-button').click(function() {
        var button = $(this);
        var boardId = button.data('board-id');
        $.ajax({
            url: '/likeBoard/toggle/' + boardId,
            type: 'POST',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(response) {
                // 좋아요 수 업데이트
                button.toggleClass('filled');
                button.closest('.heart-sec').find('.heart-count').text(response.likeCount);
            },
            error: function(xhr, status, error) {
                alert('좋아요 처리 중 오류가 발생했습니다.');
            }
        });
    });
});