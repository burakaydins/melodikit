document.querySelectorAll(".track-player").forEach((player) => {
    player.addEventListener("play", async () => {
        if (player.dataset.counted === "true") {
            return;
        }
        player.dataset.counted = "true";

        try {
            const response = await fetch(`/songs/${player.dataset.songId}/play`, {
                method: "POST",
                keepalive: true
            });
            if (!response.ok) {
                throw new Error("Play count update failed");
            }
            const data = await response.json();
            updatePlayCount(player.dataset.songId, data.playCount);
        } catch {
            player.dataset.counted = "false";
        }
    });
});

function updatePlayCount(songId, playCount) {
    if (!songId || typeof playCount !== "number") {
        return;
    }

    document.querySelectorAll(`[data-play-count-song-id="${songId}"]`).forEach((element) => {
        element.textContent = `${playCount}${element.dataset.playCountSuffix || " dinlenme"}`;
        element.classList.add("play-count-updated");
        window.setTimeout(() => element.classList.remove("play-count-updated"), 900);
    });
}
