var main = {
    init : function () {
        var _this = this;
        $('btn-save').on('click', function() {
            _this.save();
        });
        $('btn-update').on('click', function() {
            _this.update();
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
    },
    update : function () {
        var data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'PUT', // Controller에서 @PutMapping으로 선언했기 때문에 꼭 PUT 사용
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (){
            alert('글이 수정되었습니다.');
            window.location.href='/'; // 글 수정이 완료될 경우 메인페이지('/')로 이동
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },
    delete : function () {
        var id = $('#id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (){
            alert('글이 삭제되었습니다.');
            window.location.href='/'; // 글 삭제가 완료될 경우 메인페이지('/')로 이동
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    }
};

main.init();