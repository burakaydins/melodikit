document.querySelectorAll(".share-btn").forEach((button) => {
    button.addEventListener("click", async () => {
        const status = button.closest(".detail-cover")?.querySelector(".share-status");
        const shareUrl = new URL(button.dataset.shareUrl, window.location.origin).toString();

        try {
            await navigator.clipboard.writeText(shareUrl);
            if (status) {
                status.textContent = "Playlist bağlantısı kopyalandı.";
            }
        } catch {
            window.open(shareUrl, "_blank", "noopener");
            if (status) {
                status.textContent = "Bağlantı yeni sekmede açıldı.";
            }
        }
    });
});
