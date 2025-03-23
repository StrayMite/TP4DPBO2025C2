class Mahasiswa {
    private String nim, nama, Nilai, jenisKelamin;

    public Mahasiswa(String nim, String nama, String jenisKelamin, String Nilai) {
        this.nim = nim;
        this.nama = nama;
        this.Nilai = Nilai;
        this.jenisKelamin = jenisKelamin;
    }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getNilai() { return Nilai; }
    public void setNilai(String Nilai) { this.Nilai = Nilai; }
}
