var main = {
    init : function () {
        var _this = this;
        $('btn-save').on('click', function() {
            _this.save();
        });
    },
    save : function () {
        var data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (){
            alert('글이 등록되었습니다.');
            window.location.href='/'; // 글 등록이 완료될 경우 메인페이지('/')로 이동
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    }
};

main.init();