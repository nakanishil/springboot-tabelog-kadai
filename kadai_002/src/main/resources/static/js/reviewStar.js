// 星評価スクリプト
document.addEventListener('DOMContentLoaded', function () {
    const stars = document.querySelectorAll('.post-stars-container .star');
    const evaluationInput = document.getElementById('evaluation');

    // 星をクリックしたときの処理
    stars.forEach(star => {
        star.addEventListener('click', function () {
            const value = this.getAttribute('data-value');
            evaluationInput.value = value; // 評価値を隠しフィールドに設定

            // 全ての星をリセット
            stars.forEach(s => s.classList.remove('selected'));

            // 選択した星までを強調表示
            for (let i = 0; i < value; i++) {
                stars[i].classList.add('selected');
            }
        });
    });
});
