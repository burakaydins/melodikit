package com.example.playlistmanager.config;

import com.example.playlistmanager.model.Album;
import com.example.playlistmanager.model.Artist;
import com.example.playlistmanager.model.Genre;
import com.example.playlistmanager.model.Playlist;
import com.example.playlistmanager.model.Song;
import com.example.playlistmanager.repository.AlbumRepository;
import com.example.playlistmanager.repository.ArtistRepository;
import com.example.playlistmanager.repository.GenreRepository;
import com.example.playlistmanager.repository.PlaylistRepository;
import com.example.playlistmanager.repository.SongRepository;
import com.example.playlistmanager.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    private final AppUserService appUserService;

    public DataInitializer(ArtistRepository artistRepository,
                           AlbumRepository albumRepository,
                           GenreRepository genreRepository,
                           SongRepository songRepository,
                           PlaylistRepository playlistRepository,
                           AppUserService appUserService) {
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.genreRepository = genreRepository;
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
        this.appUserService = appUserService;
    }

    @Override
    public void run(String... args) {
        appUserService.createDemoUserIfMissing();

        if (songRepository.count() > 0) {
            return;
        }

        Genre pop = createGenre("Pop", "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?auto=format&fit=crop&w=900&q=80");
        Genre rock = createGenre("Rock", "https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?auto=format&fit=crop&w=900&q=80");
        Genre electronic = createGenre("Elektronik", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=900&q=80");

        String sezenPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Sezen_Aksu_(Minik_Ser%C3%A7e)_(cropped).jpg?width=800";
        String queenPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Queen_A_Night_At_The_Opera_(1975_Elektra_publicity_photo_02).jpg?width=800";
        String daftPunkPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Daft_Punk_in_2013.jpg?width=800";

        Artist sezen = createArtist("Sezen Aksu", "Türkiye", sezenPhoto,
                "Türk pop müziğinin en önemli isimlerinden biri olarak kabul edilir. Söz yazarlığı, besteleri ve güçlü yorumuyla birçok sanatçıya ilham vermiştir.");
        Artist queen = createArtist("Queen", "İngiltere", queenPhoto,
                "Rock tarihinin en etkili gruplarından biridir. Sahne enerjisi, çok sesli vokalleri ve zamansız şarkılarıyla geniş bir dinleyici kitlesine ulaşmıştır.");
        Artist daftPunk = createArtist("Daft Punk", "Fransa", daftPunkPhoto,
                "Elektronik müziği pop kültürle birleştiren ikili, robot imajı ve yenilikçi prodüksiyon anlayışıyla modern dans müziğinde kalıcı iz bırakmıştır.");

        String gulumseCover = "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/75/62/48/75624807-5b53-72a5-8926-cee76fa52091/25UMGIM92899.rgb.jpg/600x600bb.jpg";
        String bohemianCover = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/e8/f1/51/e8f151ae-0f87-a2fd-b981-807a01b24504/18UMGIM55031.rgb.jpg/600x600bb.jpg";
        String digitalLoveCover = "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/fd/4a/77/fd4a77db-0ebc-d043-41a2-f32fa1bb0fb4/dj.qrikkdwj.jpg/600x600bb.jpg";
        String yazGunuCover = "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/2c/d1/d3/2cd1d39d-35b2-8971-480f-5441e0ce6a24/25UMGIM92896.rgb.jpg/600x600bb.jpg";

        Album sezenGulumse = createAlbum("Gülümse", 1991, sezen, gulumseCover);
        Album sezenGit = createAlbum("Git", 1986, sezen, yazGunuCover);
        Album queenNightOpera = createAlbum("A Night at the Opera", 1975, queen, bohemianCover);
        Album daftPunkDiscovery = createAlbum("Discovery", 2001, daftPunk, digitalLoveCover);

        Song song1 = createSong("Gülümse", sezen, sezenGulumse, pop, 238, 5, true, gulumseCover, null);
        Song song2 = createSong("Bohemian Rhapsody", queen, queenNightOpera, rock, 354, 5, true, bohemianCover, null);
        Song song3 = createSong("Digital Love", daftPunk, daftPunkDiscovery, electronic, 301, 4, false, digitalLoveCover, null);
        Song song4 = createSong("Yalnızca Sitem", sezen, sezenGit, pop, 247, 3, false, yazGunuCover, null);
        List<Song> extraSongs = createAdditionalSongs(pop, rock, electronic);

        Playlist favorites = new Playlist();
        favorites.setName("Haftanın Favorileri");
        favorites.setDescription("Sunumda göstermek için hazır örnek playlist.");
        favorites.setImageUrl("https://images.unsplash.com/photo-1506157786151-b8491531f063?auto=format&fit=crop&w=800&q=80");
        favorites.setSongs(List.of(song1, song2, song3, extraSongs.get(0), extraSongs.get(18), extraSongs.get(26)));
        playlistRepository.save(favorites);

        Playlist calm = new Playlist();
        calm.setName("Akşam Dinleme");
        calm.setDescription("Daha sakin bir müzik akışı.");
        calm.setImageUrl("https://images.unsplash.com/photo-1487180144351-b8472da7d491?auto=format&fit=crop&w=800&q=80");
        calm.setSongs(List.of(song1, song4, extraSongs.get(9), extraSongs.get(23), extraSongs.get(35)));
        playlistRepository.save(calm);
    }

    private List<Song> createAdditionalSongs(Genre pop, Genre rock, Genre electronic) {
        Genre turkishRock = createGenre("Türkçe Rock", "https://images.unsplash.com/photo-1521337581100-8ca9a73a5f79?auto=format&fit=crop&w=900&q=80");
        Genre dancePop = createGenre("Dance Pop", "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?auto=format&fit=crop&w=900&q=80");
        Genre alternative = createGenre("Alternatif", "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=900&q=80");
        Genre rb = createGenre("R&B", "https://images.unsplash.com/photo-1485579149621-3123dd979885?auto=format&fit=crop&w=900&q=80");

        String simarikCover = "https://is1-ssl.mzstatic.com/image/thumb/Music113/v4/f1/95/04/f1950424-2ae5-55ff-580d-d52e8300ec1b/dj.iedkotfi.jpg/600x600bb.jpg";
        String kuzuKuzuCover = "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/35/8d/22/358d22cf-8b9c-1e7d-996d-85aad739a255/dj.nixigvoo.jpg/600x600bb.jpg";
        String askCover = "https://is1-ssl.mzstatic.com/image/thumb/Music71/v4/bf/3e/36/bf3e3620-b844-c4d0-14d2-b24cea47a73c/886446254043.jpg/600x600bb.jpg";
        String everywayCover = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/be/6f/29/be6f29a5-084e-5c77-9fdf-3fac760800e2/886970894920.jpg/600x600bb.jpg";
        String eleGuneCover = "https://is1-ssl.mzstatic.com/image/thumb/Music/v4/7f/8a/6a/7f8a6a80-10a5-dca5-8a66-4b30130c7b38/cover.jpg/600x600bb.jpg";
        String sudeCover = "https://is1-ssl.mzstatic.com/image/thumb/Music71/v4/b4/a4/a7/b4a4a75f-ac5f-529e-4b04-5e6c724af320/cover.jpg/600x600bb.jpg";
        String donenceCover = "https://is1-ssl.mzstatic.com/image/thumb/Music/v4/73/82/5c/73825cf7-0720-518f-dbb2-9d9905a96d3f/cover.jpg/600x600bb.jpg";
        String gulpembeCover = "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/96/b5/6f/96b56f34-4bb9-3aa5-add2-5a2504e74562/cover.jpg/600x600bb.jpg";
        String antidepresanCover = "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/cd/6a/fb/cd6afb23-3442-e7ab-3b39-46f458bcad40/196922249655_Cover.jpg/600x600bb.jpg";
        String sarmasikCover = "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/21/23/fa/2123fa27-58d7-94b0-1569-0e08184e2da1/cover.jpg/600x600bb.jpg";
        String birDerdimVarCover = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/e7/96/e5/e796e52e-030e-fd01-096a-0264fcaa3f9a/cover.jpg/600x600bb.jpg";
        String cambazCover = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/e7/96/e5/e796e52e-030e-fd01-096a-0264fcaa3f9a/cover.jpg/600x600bb.jpg";
        String sendenDahaGuzelCover = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/e3/6a/03/e36a0347-7191-da04-7d53-32569e5904fd/884977122039.jpg/600x600bb.jpg";
        String herSeyiYakCover = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/ba/22/76/ba2276a9-e829-2a71-1ed8-43495f0d0a11/00602567788485.rgb.jpg/600x600bb.jpg";
        String paramparcaCover = "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/e1/06/ee/e106ee25-82c4-244f-11ce-f3f81b92461d/00602567853671.rgb.jpg/600x600bb.jpg";
        String istanbuldaSonbaharCover = "https://is1-ssl.mzstatic.com/image/thumb/Music118/v4/9d/b7/f7/9db7f706-9c6f-5061-d785-052ca2dca606/cover.jpg/600x600bb.jpg";
        String benBoyleyimCover = "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/e1/15/0c/e1150cc6-92ff-cc88-8be2-ea1b7aac55ab/cover.jpg/600x600bb.jpg";
        String kafamaGoreCover = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/c2/1b/64/c21b6411-c94e-319b-5190-e972bb2c9ebb/cover.jpg/600x600bb.jpg";
        String blindingLightsCover = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/a6/6e/bf/a66ebf79-5008-8948-b352-a790fc87446b/19UM1IM04638.rgb.jpg/600x600bb.jpg";
        String saveYourTearsCover = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/2b/b9/fe/2bb9fef5-d7f3-8345-25a9-db0e79fde4e4/20UMGIM11048.rgb.jpg/600x600bb.jpg";
        String levitatingCover = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/25/cd/d4/25cdd423-4a23-1913-04ed-921a58cccb7d/190295000639.jpg/600x600bb.jpg";
        String dontStartNowCover = "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/6c/11/d6/6c11d681-aa3a-d59e-4c2e-f77e181026ab/190295092665.jpg/600x600bb.jpg";
        String rollingInTheDeepCover = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/7a/80/6a/7a806a9a-ba56-57c0-f6c7-59cfc1ca2abf/634904852160.png/600x600bb.jpg";
        String helloCover = "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/62/bc/87/62bc8791-2a12-4b01-8928-d601684a951c/634904074005.png/600x600bb.jpg";
        String badGuyCover = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/1a/37/d1/1a37d1b1-8508-54f2-f541-bf4e437dda76/19UMGIM05028.rgb.jpg/600x600bb.jpg";
        String oceanEyesCover = "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/2e/c1/e3/2ec1e3e2-12f5-2bc2-f9d5-b102d9efcb89/194491997700.jpg/600x600bb.jpg";
        String yellowCover = "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/f5/93/8c/f5938c49-964c-31d1-4b33-78b634f71fb7/190295978075.jpg/600x600bb.jpg";
        String vivaLaVidaCover = "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/56/bc/3d/56bc3d9c-0a4a-0466-643e-eb9bd748e8bf/190295978037.jpg/600x600bb.jpg";
        String doIWannaKnowCover = "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/69/9c/b5/699cb5d6-115c-ff73-9d26-e57ea4350d72/887828031795.png/600x600bb.jpg";
        String dancefloorCover = "https://is1-ssl.mzstatic.com/image/thumb/Features125/v4/cf/9b/96/cf9b9637-f619-eceb-5382-e9b4d44e74fb/dj.npwkgmai.jpg/600x600bb.jpg";
        String teenSpiritCover = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/32/03/d2/3203d294-c0a0-a016-ddcb-508a093aa0b0/11UMGIM26899.rgb.jpg/600x600bb.jpg";
        String comeAsYouAreCover = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/95/fd/b9/95fdb9b2-6d2b-92a6-97f2-51c1a6d77f1a/00602527874609.rgb.jpg/600x600bb.jpg";
        String nothingElseMattersCover = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/d4/d2/e1/d4d2e187-0d4b-a32c-b7e8-913af8afba2b/21UMGIM48676.rgb.jpg/600x600bb.jpg";
        String enterSandmanCover = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/d4/d2/e1/d4d2e187-0d4b-a32c-b7e8-913af8afba2b/21UMGIM48676.rgb.jpg/600x600bb.jpg";
        String anotherBrickCover = "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/3e/17/ec/3e17ec6d-f980-c64f-19e0-a6fd8bbf0c10/886445635850.jpg/600x600bb.jpg";
        String wishYouWereHereCover = "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/3f/12/0e/3f120eeb-f199-e4e0-e54e-95eb95961e50/886445636093.jpg/600x600bb.jpg";
        String billieJeanCover = "https://is1-ssl.mzstatic.com/image/thumb/Features125/v4/2a/82/a5/2a82a59b-6e79-7c39-761a-c1d3b82e93cb/dj.dzbxcmnk.jpg/600x600bb.jpg";
        String beatItCover = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/32/4f/fd/324ffda2-9e51-8f6a-0c2d-c6fd2b41ac55/074643811224.jpg/600x600bb.jpg";
        String likeAPrayerCover = "https://is1-ssl.mzstatic.com/image/thumb/Music114/v4/20/3c/f5/203cf53d-689e-528f-29d7-ba33758254aa/mzi.rotbotfl.jpg/600x600bb.jpg";
        String frozenCover = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/dd/2b/8d/dd2b8d84-e032-94d2-473a-3f8efd18fe36/dj.rwfgroxa.jpg/600x600bb.jpg";

        String tarkanPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Tarkan_(9).jpg?width=800";
        String sertabPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Sertab_Erener_-_15._Radyo_Bo%C4%9Fazi%C3%A7i_%C3%96d%C3%BClleri_(cropped).jpg?width=800";
        String mfoPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Mfo%20jolly%20konser.png?width=800";
        String barisPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Bar%C4%B1%C5%9F%20Man%C3%A7o%20cropped.JPG?width=800";
        String mabelPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Mabel%20Matiz%20-%2015.%20Radyo%20Bo%C4%9Fazi%C3%A7i%20%C3%96d%C3%BClleri%20%281%29%20-%20cropped.jpg?width=800";
        String morVeOtesiPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Mor%20ve%20%C3%96tesi%2C%20Turkey%2C%20Eurovision%202008.jpg?width=800";
        String dumanPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Duman%20Grubu.jpg?width=800";
        String teomanPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Teoman%20P1360225.jpg?width=800";
        String athenaPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Athena%20Band%28G%C3%B6khan%20%C3%96zo%C4%9Fuz%29.JPG?width=800";
        String theWeekndPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/The%20Weeknd%20Portrait%20by%20Brian%20Ziff.jpg?width=800";
        String duaLipaPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Dua%20Lipa-69798%20%28cropped%29.jpg?width=800";
        String adelePhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Adele%202016.jpg?width=800";
        String billiePhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/BillieEilishO2140725-39%20-%2054665577407%20%28cropped%29.jpg?width=800";
        String coldplayPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/ColdplayWembley120925%20%28cropped%29.jpg?width=800";
        String arcticMonkeysPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Arctic%20Monkeys%20-%20Orange%20Stage%20-%20Roskilde%20Festival%202014.jpg?width=800";
        String nirvanaPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Nirvana%20around%201992.jpg?width=800";
        String metallicaPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Metallica%20March%202024.jpg?width=800";
        String pinkFloydPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/PinkFloyd1973%20retouched.jpg?width=800";
        String michaelPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Michael%20Jackson%201983%20%283x4%20cropped%29%20%28contrast%29.jpg?width=800";
        String madonnaPhoto = "https://commons.wikimedia.org/wiki/Special:FilePath/Madonna%20Rebel%20Heart%20Tour%202015%20-%20Stockholm%20%2823051472299%29%20%28cropped%29.jpg?width=800";

        Artist tarkan = createArtist("Tarkan", "Türkiye", tarkanPhoto, "Türkçe pop sahnesinin güçlü vokali ve sahne enerjisiyle öne çıkan isimlerinden biridir.");
        Artist sertab = createArtist("Sertab Erener", "Türkiye", sertabPhoto, "Pop, rock ve güçlü vokal performanslarını birleştiren ödüllü bir sanatçıdır.");
        Artist mfo = createArtist("MFÖ", "Türkiye", mfoPhoto, "Çok sesli vokalleri ve mizahi diliyle Türk pop-rock tarihinde özel bir yere sahiptir.");
        Artist baris = createArtist("Barış Manço", "Türkiye", barisPhoto, "Anadolu rock kültürünün en sevilen temsilcilerindendir.");
        Artist mabel = createArtist("Mabel Matiz", "Türkiye", mabelPhoto, "Modern Türkçe popta şiirsel sözleri ve özgün sound'uyla tanınır.");
        Artist morVeOtesi = createArtist("Mor ve Ötesi", "Türkiye", morVeOtesiPhoto, "Alternatif rock sound'u ve güçlü şarkı sözleriyle bilinen bir gruptur.");
        Artist duman = createArtist("Duman", "Türkiye", dumanPhoto, "Türkçe rock sahnesinde kendine has vokali ve gitar tonuyla öne çıkar.");
        Artist teoman = createArtist("Teoman", "Türkiye", teomanPhoto, "Şehirli hikayeleri ve melankolik anlatımıyla tanınan bir rock sanatçısıdır.");
        Artist athena = createArtist("Athena", "Türkiye", athenaPhoto, "Ska, punk ve rock etkilerini enerjik sahne performansıyla birleştiren bir gruptur.");
        Artist theWeeknd = createArtist("The Weeknd", "Kanada", theWeekndPhoto, "Modern pop ve R&B üretiminde sinematik sound'uyla öne çıkan bir sanatçıdır.");
        Artist duaLipa = createArtist("Dua Lipa", "İngiltere", duaLipaPhoto, "Dans-pop sound'unu güçlü vokallerle birleştiren global pop yıldızıdır.");
        Artist adele = createArtist("Adele", "İngiltere", adelePhoto, "Duygusal vokal performansları ve güçlü baladlarıyla tanınır.");
        Artist billie = createArtist("Billie Eilish", "ABD", billiePhoto, "Minimal prodüksiyonları ve atmosferik vokal tarzıyla dikkat çeker.");
        Artist coldplay = createArtist("Coldplay", "İngiltere", coldplayPhoto, "Arena rock, pop ve elektronik öğeleri birleştiren geniş kitleli bir gruptur.");
        Artist arcticMonkeys = createArtist("Arctic Monkeys", "İngiltere", arcticMonkeysPhoto, "Indie rock sahnesinin keskin gitarları ve karakteristik vokaliyle bilinen grubudur.");
        Artist nirvana = createArtist("Nirvana", "ABD", nirvanaPhoto, "Grunge müziğin dünya çapında tanınmasını sağlayan etkili gruplardan biridir.");
        Artist metallica = createArtist("Metallica", "ABD", metallicaPhoto, "Metal müziğin en büyük ve en etkili gruplarından biridir.");
        Artist pinkFloyd = createArtist("Pink Floyd", "İngiltere", pinkFloydPhoto, "Progresif rock tarihinin deneysel ve atmosferik gruplarından biridir.");
        Artist michael = createArtist("Michael Jackson", "ABD", michaelPhoto, "Pop müziğin en ikonik performans ve prodüksiyon figürlerinden biridir.");
        Artist madonna = createArtist("Madonna", "ABD", madonnaPhoto, "Pop kültürü ve dans müziği üzerinde büyük etkisi olan bir sanatçıdır.");

        Album tarkanOlurumSana = createAlbum("Ölürüm Sana", 1997, tarkan, simarikCover);
        Album tarkanKarma = createAlbum("Karma", 2001, tarkan, kuzuKuzuCover);
        Album sertabSelfTitled = createAlbum("Sertab Erener", 1999, sertab, askCover);
        Album sertabBestOf = createAlbum("The Best of Sertab Erener", 2007, sertab, everywayCover);
        Album mfoSingle = createAlbum("MFÖ - Single", 2003, mfo, eleGuneCover);
        Album mfoCollection = createAlbum("MFÖ Collection", 2003, mfo, sudeCover);
        Album barisMancoloji = createAlbum("Mançoloji 2", 1999, baris, donenceCover);
        Album barisHalHal = createAlbum("Hal Hal", 1989, baris, gulpembeCover);
        Album mabelAntidepresan = createAlbum("Antidepresan - Single", 2022, mabel, antidepresanCover);
        Album mabelMaya = createAlbum("Maya", 2018, mabel, sarmasikCover);
        Album morDunya = createAlbum("Dünya Yalan Söylüyor", 2004, morVeOtesi, birDerdimVarCover);
        Album dumanTwo = createAlbum("Duman II", 2009, duman, sendenDahaGuzelCover);
        Album dumanBelki = createAlbum("Belki Alışman Lazım", 2002, duman, herSeyiYakCover);
        Album teomanSeventeen = createAlbum("17", 2000, teoman, paramparcaCover);
        Album teomanKoyuAntoloji = createAlbum("Koyu Antoloji", 2018, teoman, istanbuldaSonbaharCover);
        Album athenaSingle = createAlbum("Ben Böyleyim - Single", 2011, athena, benBoyleyimCover);
        Album athenaAltust = createAlbum("Altüst", 2014, athena, kafamaGoreCover);
        Album weekndAfterHours = createAlbum("After Hours", 2020, theWeeknd, saveYourTearsCover);
        Album duaFutureNostalgia = createAlbum("Future Nostalgia", 2020, duaLipa, dontStartNowCover);
        Album adeleTwentyOne = createAlbum("21", 2011, adele, rollingInTheDeepCover);
        Album adeleTwentyFive = createAlbum("25", 2015, adele, helloCover);
        Album billieAsleep = createAlbum("WHEN WE ALL FALL ASLEEP, WHERE DO WE GO?", 2019, billie, badGuyCover);
        Album billieOceanEyes = createAlbum("Ocean Eyes - Single", 2015, billie, oceanEyesCover);
        Album coldplayParachutes = createAlbum("Parachutes", 2000, coldplay, yellowCover);
        Album coldplayViva = createAlbum("Viva la Vida or Death and All His Friends", 2008, coldplay, vivaLaVidaCover);
        Album arcticAm = createAlbum("AM", 2013, arcticMonkeys, doIWannaKnowCover);
        Album arcticDebut = createAlbum("Whatever People Say I Am, That's What I'm Not", 2006, arcticMonkeys, dancefloorCover);
        Album nirvanaNevermind = createAlbum("Nevermind", 1991, nirvana, teenSpiritCover);
        Album metallicaBlackAlbum = createAlbum("Metallica", 1991, metallica, nothingElseMattersCover);
        Album pinkFloydWall = createAlbum("The Wall", 1979, pinkFloyd, anotherBrickCover);
        Album pinkFloydWish = createAlbum("Wish You Were Here", 1975, pinkFloyd, wishYouWereHereCover);
        Album michaelThriller = createAlbum("Thriller", 1982, michael, beatItCover);
        Album madonnaPrayer = createAlbum("Like a Prayer", 1989, madonna, likeAPrayerCover);
        Album madonnaRay = createAlbum("Ray of Light", 1998, madonna, frozenCover);

        return List.of(
                createSong("Şımarık", tarkan, tarkanOlurumSana, pop, 235, 5, true, simarikCover, null),
                createSong("Kuzu Kuzu", tarkan, tarkanKarma, pop, 229, 5, false, kuzuKuzuCover, null),
                createSong("Aşk", sertab, sertabSelfTitled, pop, 244, 4, false, askCover, null),
                createSong("Everyway That I Can", sertab, sertabBestOf, dancePop, 181, 5, true, everywayCover, null),
                createSong("Ele Güne Karşı", mfo, mfoSingle, pop, 258, 4, false, eleGuneCover, null),
                createSong("Sude", mfo, mfoCollection, pop, 249, 4, false, sudeCover, null),
                createSong("Dönence", baris, barisMancoloji, rock, 405, 5, true, donenceCover, null),
                createSong("Gülpembe", baris, barisHalHal, rock, 278, 5, false, gulpembeCover, null),
                createSong("Antidepresan", mabel, mabelAntidepresan, pop, 205, 4, true, antidepresanCover, null),
                createSong("Sarmaşık", mabel, mabelMaya, pop, 236, 4, false, sarmasikCover, null),
                createSong("Bir Derdim Var", morVeOtesi, morDunya, turkishRock, 253, 5, true, birDerdimVarCover, null),
                createSong("Cambaz", morVeOtesi, morDunya, turkishRock, 228, 4, false, cambazCover, null),
                createSong("Senden Daha Güzel", duman, dumanTwo, turkishRock, 284, 5, true, sendenDahaGuzelCover, null),
                createSong("Her Şeyi Yak", duman, dumanBelki, turkishRock, 251, 4, false, herSeyiYakCover, null),
                createSong("Paramparça", teoman, teomanSeventeen, turkishRock, 243, 5, true, paramparcaCover, null),
                createSong("İstanbul'da Sonbahar", teoman, teomanKoyuAntoloji, turkishRock, 271, 5, false, istanbuldaSonbaharCover, null),
                createSong("Ben Böyleyim", athena, athenaSingle, turkishRock, 214, 4, false, benBoyleyimCover, null),
                createSong("Kafama Göre", athena, athenaAltust, turkishRock, 198, 4, false, kafamaGoreCover, null),
                createSong("Blinding Lights", theWeeknd, weekndAfterHours, rb, 200, 5, true, blindingLightsCover, null),
                createSong("Save Your Tears", theWeeknd, weekndAfterHours, rb, 215, 4, false, saveYourTearsCover, null),
                createSong("Levitating", duaLipa, duaFutureNostalgia, dancePop, 203, 5, true, levitatingCover, null),
                createSong("Don't Start Now", duaLipa, duaFutureNostalgia, dancePop, 183, 4, false, dontStartNowCover, null),
                createSong("Rolling in the Deep", adele, adeleTwentyOne, pop, 228, 5, true, rollingInTheDeepCover, null),
                createSong("Hello", adele, adeleTwentyFive, pop, 295, 5, false, helloCover, null),
                createSong("Bad Guy", billie, billieAsleep, alternative, 194, 4, true, badGuyCover, null),
                createSong("Ocean Eyes", billie, billieOceanEyes, alternative, 200, 4, false, oceanEyesCover, null),
                createSong("Yellow", coldplay, coldplayParachutes, alternative, 267, 5, true, yellowCover, null),
                createSong("Viva La Vida", coldplay, coldplayViva, alternative, 242, 5, false, vivaLaVidaCover, null),
                createSong("Do I Wanna Know?", arcticMonkeys, arcticAm, alternative, 272, 5, true, doIWannaKnowCover, null),
                createSong("I Bet You Look Good on the Dancefloor", arcticMonkeys, arcticDebut, alternative, 173, 4, false, dancefloorCover, null),
                createSong("Smells Like Teen Spirit", nirvana, nirvanaNevermind, rock, 301, 5, true, teenSpiritCover, null),
                createSong("Come As You Are", nirvana, nirvanaNevermind, rock, 219, 5, false, comeAsYouAreCover, null),
                createSong("Nothing Else Matters", metallica, metallicaBlackAlbum, rock, 388, 5, true, nothingElseMattersCover, null),
                createSong("Enter Sandman", metallica, metallicaBlackAlbum, rock, 331, 4, false, enterSandmanCover, null),
                createSong("Another Brick in the Wall", pinkFloyd, pinkFloydWall, rock, 239, 5, true, anotherBrickCover, null),
                createSong("Wish You Were Here", pinkFloyd, pinkFloydWish, rock, 334, 5, false, wishYouWereHereCover, null),
                createSong("Billie Jean", michael, michaelThriller, dancePop, 294, 5, true, billieJeanCover, null),
                createSong("Beat It", michael, michaelThriller, dancePop, 258, 5, false, beatItCover, null),
                createSong("Like a Prayer", madonna, madonnaPrayer, dancePop, 342, 4, true, likeAPrayerCover, null),
                createSong("Frozen", madonna, madonnaRay, electronic, 377, 4, false, frozenCover, null)
        );
    }

    private Genre createGenre(String name, String imageUrl) {
        Genre genre = new Genre();
        genre.setName(name);
        genre.setImageUrl(imageUrl);
        return genreRepository.save(genre);
    }

    private Artist createArtist(String name, String country, String imageUrl, String biography) {
        Artist artist = new Artist();
        artist.setName(name);
        artist.setCountry(country);
        artist.setImageUrl(imageUrl);
        artist.setBiography(biography);
        return artistRepository.save(artist);
    }

    private Album createAlbum(String title, Integer releaseYear, Artist artist, String imageUrl) {
        Album album = new Album();
        album.setTitle(title);
        album.setReleaseYear(releaseYear);
        album.setArtist(artist);
        album.setImageUrl(imageUrl);
        return albumRepository.save(album);
    }

    private Song createSong(String title, Artist artist, Album album, Genre genre, int duration, int rating, boolean favorite, String imageUrl, String audioUrl) {
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setAlbum(album);
        song.setGenre(genre);
        song.setDuration(duration);
        song.setRating(rating);
        song.setFavorite(favorite);
        song.setImageUrl(imageUrl);
        song.setAudioUrl(audioUrl);
        song.setPlayCount(rating * 2);
        Song savedSong = songRepository.save(song);
        if (savedSong.getAudioUrl() == null || savedSong.getAudioUrl().isBlank()) {
            savedSong.setAudioUrl("/songs/" + savedSong.getId() + "/preview");
            return songRepository.save(savedSong);
        }
        return savedSong;
    }
}
