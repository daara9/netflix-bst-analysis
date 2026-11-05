// Integrantes: Danilo Oliveira Santos - RA 10410905
//              Miguel Batista da Silva - RA 10736250

import java.util.Locale;

public class ProgramaNetFlix {
    private String id;
    private String title;
    private String type;
    private String description;
    private int releaseYear;
    private String ageCertification;
    private int runtime;
    private String genres;
    private String productionCountries;
    private int seasons;
    private String imdbId;
    private double imdbScore;
    private int imdbVotes;
    private double tmdbPopularity;
    private double tmdbScore;

    public ProgramaNetFlix(String id, String title, String type, String description,
            int releaseYear, String ageCertification, int runtime, String genres,
            String productionCountries, int seasons, String imdbId, double imdbScore,
            int imdbVotes, double tmdbPopularity, double tmdbScore) {
        this.id = id; this.title = title; this.type = type; this.description = description;
        this.releaseYear = releaseYear; this.ageCertification = ageCertification;
        this.runtime = runtime; this.genres = genres; this.productionCountries = productionCountries;
        this.seasons = seasons; this.imdbId = imdbId; this.imdbScore = imdbScore;
        this.imdbVotes = imdbVotes; this.tmdbPopularity = tmdbPopularity; this.tmdbScore = tmdbScore;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public String getAgeCertification() { return ageCertification; }
    public void setAgeCertification(String ageCertification) { this.ageCertification = ageCertification; }
    public int getRuntime() { return runtime; }
    public void setRuntime(int runtime) { this.runtime = runtime; }
    public String getGenres() { return genres; }
    public void setGenres(String genres) { this.genres = genres; }
    public String getProductionCountries() { return productionCountries; }
    public void setProductionCountries(String productionCountries) { this.productionCountries = productionCountries; }
    public int getSeasons() { return seasons; }
    public void setSeasons(int seasons) { this.seasons = seasons; }
    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }
    public double getImdbScore() { return imdbScore; }
    public void setImdbScore(double imdbScore) { this.imdbScore = imdbScore; }
    public int getImdbVotes() { return imdbVotes; }
    public void setImdbVotes(int imdbVotes) { this.imdbVotes = imdbVotes; }
    public double getTmdbPopularity() { return tmdbPopularity; }
    public void setTmdbPopularity(double tmdbPopularity) { this.tmdbPopularity = tmdbPopularity; }
    public double getTmdbScore() { return tmdbScore; }
    public void setTmdbScore(double tmdbScore) { this.tmdbScore = tmdbScore; }

    public String toString() {
        return String.format(Locale.US,
            "ID: %s | Título: %s | Tipo: %s%nAno: %d | Classificação: %s | Duração: %d min%nGêneros: %s | Países: %s%nTemporadas: %d | IMDb: %s (%.1f / %d votos)%nTMDB: popularidade %.3f | nota %.2f%nDescrição: %s",
            id, title, type, releaseYear, ageCertification, runtime, genres, productionCountries,
            seasons, imdbId, imdbScore, imdbVotes, tmdbPopularity, tmdbScore, description);
    }

    public String toCsv() {
        return String.join(",",
            csv(id), csv(title), csv(type), csv(description), String.valueOf(releaseYear),
            csv(ageCertification), String.valueOf(runtime), csv(genres), csv(productionCountries),
            String.valueOf(seasons), csv(imdbId), String.valueOf(imdbScore), String.valueOf(imdbVotes),
            String.valueOf(tmdbPopularity), String.valueOf(tmdbScore));
    }

    private static String csv(String value) {
        if (value == null) value = "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
