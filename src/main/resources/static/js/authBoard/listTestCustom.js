$(document).ready(function() {
    var csrfToken = $('meta[name="_csrf"]').attr('content');
    var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    $('.heart-button').each(function() {
        var button = $(this);
        var boardId = button.data('board-id');
        $.ajax({
            url: '/likeBoard/status/' + boardId,
            type: 'GET',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(response) {
                if (response.isLiked) {
                    button.addClass('filled');
                } else {
                    button.removeClass('filled');
                }
            },
            error: function(xhr, status, error) {
                console.error('좋아요 상태를 가져오는 중 오류가 발생했습니다:', error);
            }
        });
    });

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
                console.error('좋아요 처리 중 오류가 발생했습니다:', status, error);
                alert('로그인 후 이용하시기 바랍니다.');
            }
        });
    });
});
