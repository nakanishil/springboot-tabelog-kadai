/**
 * CSVファイル出力用
 * 
 * @author SAMURAI
 * @since 2024-12-25 
 */
/* 飲食店一覧 */
$(document).on('submit','form[action="/admin/restaurants/csv"]', function() {
    let inputValue = $('input[name="keyword"]').val();
    console.log('入力された値: ' + inputValue);
    $('input[name="keyword2"]').val(inputValue);
});
