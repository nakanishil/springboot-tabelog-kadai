// flatpickr.js
document.addEventListener("DOMContentLoaded", function() {
    flatpickr("#appointmentDate", {
        enableTime: true, // 時間選択を有効化
        dateFormat: "Y-m-d H:i", // 日付と時間のフォーマット
        minDate: "today", // 今日以降の日付のみ選択可能
        time_24hr: true, // 24時間形式を使用
        locale: "ja" // 日本語ロケール
    });
});
