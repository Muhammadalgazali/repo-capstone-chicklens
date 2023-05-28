const express = require('express');
const app = express();
const port = process.env.PORT || 80;
const bodyParser = require('body-parser');


const mysql = require('mysql2');
const connection = mysql.createConnection({
    host: '34.101.96.72',
    user: 'root',
    password: 'root',
    database: 'latihan'
});

// Menambahkan middleware body-parser di sini
app.use(bodyParser.json());

app.listen(port, () => {
    console.log(`Capstone backend listening on port ${port}`);
});

app.get('/', (req, res) => {
    res.send('Hello Algazali');
});

//gambar1 // data terakhir
app.get('/gambar1', (req, res) => {
    connection.query('SELECT * FROM ayam ORDER BY no DESC LIMIT 1', (error, results) => {
        if (error) {
            res.status(500).json({ error: 'Terjadi kesalahan saat mengambil data dari database.' });
        } else {
            if (results.length > 0) {
                const result = results[0];
                const tanggal = new Date(result.tanggal).toLocaleDateString('en-GB');

                const data = {
                    no: result.no,
                    tanggal: tanggal,
                    jam: result.jam,
                    jumlah_ayam: result.jumlah_ayam,
                    pakan: result.jumlah_pakan,
                    gambar: result.gambar
                };

                res.json(data);
            } else {
                res.status(404).json({ error: 'Data tidak ditemukan.' });
            }
        }
    });
});


//gambar2 semua data
app.get('/gambar2', (req, res) => {
    connection.query('SELECT * FROM ayam', (error, results) => {
        if (error) {
            res.status(500).json({ error: 'Terjadi kesalahan saat mengambil data dari database.' });
        } else {
            const data = results.map((result, index) => {
                return {
                    no: index + 1,
                    tanggal: result.tanggal,
                    jam: result.jam,
                    jumlah_ayam: result.jumlah_ayam,
                    pakan: result.jumlah_pakan,
                    gambar: result.gambar
                };
            });

            res.json(data);
        }
    });
});

//gambar3?no=4 // data khusus
app.get('/gambar3', (req, res) => {
    const no = req.query.no; // Mendapatkan nilai parameter 'no' dari URL

    connection.query('SELECT no, jam, tanggal, jumlah_ayam, jumlah_pakan, gambar FROM ayam WHERE no = ?', [no], (error, results) => {
        if (error) {
            res.status(500).json({ error: 'Terjadi kesalahan saat mengambil data dari database.' });
        } else {
            if (results.length > 0) {
                const data = {
                    no: results[0].no,
                    tanggal: results[0].tanggal,
                    jam: results[0].jam,
                    jumlah_ayam: results[0].jumlah_ayam,
                    pakan: results[0].jumlah_pakan,
                    gambar: results[0].gambar
                };
                res.json(data);
            } else {
                res.status(404).json({ error: 'Data tidak ditemukan.' });
            }
        }
    });
});


//gambar 4 // ambil data 4 buah per page dalam url gambar4?page=2
app.get('/gambar4', (req, res) => {
    const page = req.query.page || 1; // Mendapatkan nomor halaman dari query parameter, defaultnya 1 jika tidak ada

    const limit = 4; // Jumlah data per halaman
    const offset = (page - 1) * limit; // Offset data berdasarkan halaman

    connection.query(`SELECT * FROM ayam LIMIT ${limit} OFFSET ${offset}`, (error, results) => {
        if (error) {
            res.status(500).json({ error: 'Terjadi kesalahan saat mengambil data dari database.' });
        } else {
            const data = results.map(result => {
                return {
                    no: result.no,
                    tanggal: result.tanggal,
                    jam: result.jam,
                    jumlah_ayam: result.jumlah_ayam,
                    pakan: result.jumlah_pakan,
                    gambar: result.gambar
                };
            });

            res.json(data);
        }
    });
});

//upload data ke database format json
app.post('/upload1', (req, res) => {
    const { jam, tanggal, jumlah_ayam, jumlah_pakan, gambar } = req.body;

    // Lakukan penyimpanan data ke database
    const query = 'INSERT INTO ayam (jam, tanggal, jumlah_ayam, jumlah_pakan, gambar) VALUES (?, ?, ?, ?, ?)';
    connection.query(query, [jam, tanggal, jumlah_ayam, jumlah_pakan, gambar], (error, results) => {
        if (error) {
            res.status(500).json({ error: 'Terjadi kesalahan saat menyimpan data ke database.' });
        } else {
            res.json({ message: 'Data berhasil disimpan ke database.' });
        }
    });
});

//upload data ke database format json tanpa gambar
app.post('/upload2', (req, res) => {
    const { jam, tanggal, jumlah_ayam, jumlah_pakan} = req.body;

    // Lakukan penyimpanan data ke database
    const query = 'INSERT INTO ayam (jam, tanggal, jumlah_ayam, jumlah_pakan) VALUES (?, ?, ?, ?)';
    connection.query(query, [jam, tanggal, jumlah_ayam, jumlah_pakan], (error, results) => {
        if (error) {
            res.status(500).json({ error: 'Terjadi kesalahan saat menyimpan data ke database.' });
        } else {
            res.json({ message: 'Data berhasil disimpan ke database.' });
        }
    });
});

