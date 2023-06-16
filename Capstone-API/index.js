const express = require('express');
const app = express();
const port = process.env.PORT || 8080;
const bodyParser = require('body-parser');

// Koneksi database
const mysql = require('mysql2');
const connection = mysql.createConnection({
    host: '34.101.219.245',
    user: 'root',
    password: 'root',
    database: 'capstone_ayam'
});

// Menambahkan middleware body-parser di sini
app.use(bodyParser.json());

// Menambahkan event handler untuk 'error' pada koneksi database
connection.on('error', (err) => {
    console.error('Database error:', err);
});

// Membuka koneksi
connection.connect((err) => {
    if (err) {
        console.error('Error connecting to database:', err);
        return;
    }
    console.log('Connected to database.');

    // Lanjutkan dengan kode Anda

    // Listen port
    app.listen(port, () => {
        console.log(`Capstone backend listening on port ${port}`);
    });
});

// Route dengan path '/'
app.get('/', (req, res) => {
    res.send('Hello World!');
});

// // route

// //1. data 6 hari terakhir / last-6-day / ambil data dari 6 hari terakhir dalam data
// (harike, tanggal, rerata ayam, rerata pakan)

app.get('/last-6-day', (req, res) => {
    connection.query('SELECT harike, tanggal, AVG(ayam) AS rerata_ayam, AVG(pakan) AS rerata_pakan FROM data GROUP BY harike, tanggal ORDER BY tanggal DESC LIMIT 6;', (error, results) => {
        if (error) {
            res.status(500).json({ error: 'Terjadi kesalahan saat mengambil data dari database.' });
        } else {
            const data = results.map((result, index) => {
                const formattedDate = result.tanggal.toLocaleDateString('en-US');

                return {
                    harike: result.harike,
                    tanggal: formattedDate,
                    rerata_ayam: Number(result.rerata_ayam).toFixed(2),
                    rerata_pakan: Number(result.rerata_pakan).toFixed(2),
                };
            });

            res.json(data);
        }
    });
});


// 2. semua data / data
// (harike, tanggal, rerata ayam, rerata pakan)

app.get('/data', (req, res) => {
    connection.query('SELECT harike, tanggal, AVG(ayam) AS rerata_ayam, AVG(pakan) AS rerata_pakan FROM data GROUP BY harike, tanggal ORDER BY tanggal DESC;', (error, results) => {
        if (error) {
            res.status(500).json({ error: 'Terjadi kesalahan saat mengambil data dari database.' });
        } else {
            const data = results.map((result, index) => {
                const formattedDate = result.tanggal.toLocaleDateString('en-US');

                return {
                    harike: result.harike,
                    tanggal: formattedDate,
                    rerata_ayam: result.rerata_ayam,
                    rerata_pakan: Number(result.rerata_pakan).toFixed(2),
                };
            });

            res.json(data);
        }
    });
});

// 3. data untuk grafik perminggu /perminggu
// (minggu,rata-ayam, rata-pakan)

app.get('/perminggu', (req, res) => {
    connection.query('SELECT COALESCE(SUM(CASE WHEN harike BETWEEN 1 AND 7 THEN ayam ELSE 0 END), 0) / NULLIF(COUNT(CASE WHEN harike BETWEEN 1 AND 7 THEN ayam END), 0) AS Minggu1_ayam, COALESCE(SUM(CASE WHEN harike BETWEEN 8 AND 14 THEN ayam ELSE 0 END), 0) / NULLIF(COUNT(CASE WHEN harike BETWEEN 8 AND 14 THEN ayam END), 0) AS Minggu2_ayam, COALESCE(SUM(CASE WHEN harike BETWEEN 15 AND 21 THEN ayam ELSE 0 END), 0) / NULLIF(COUNT(CASE WHEN harike BETWEEN 15 AND 21 THEN ayam END), 0) AS Minggu3_ayam, COALESCE(SUM(CASE WHEN harike BETWEEN 22 AND 28 THEN ayam ELSE 0 END), 0) / NULLIF(COUNT(CASE WHEN harike BETWEEN 22 AND 28 THEN ayam END), 0) AS Minggu4_ayam,  COALESCE(SUM(CASE WHEN harike BETWEEN 29 AND 35 THEN ayam ELSE 0 END), 0) / NULLIF(COUNT(CASE WHEN harike BETWEEN 29 AND 35 THEN ayam END), 0) AS Minggu5_ayam, COALESCE(SUM(CASE WHEN harike BETWEEN 1 AND 7 THEN pakan ELSE 0 END), 0) / NULLIF(COUNT(CASE WHEN harike BETWEEN 1 AND 7 THEN pakan END), 0) AS Minggu1_pakan, COALESCE(SUM(CASE WHEN harike BETWEEN 8 AND 14 THEN pakan ELSE 0 END), 0) / NULLIF(COUNT(CASE WHEN harike BETWEEN 8 AND 14 THEN pakan END), 0) AS Minggu2_pakan, COALESCE(SUM(CASE WHEN harike BETWEEN 15 AND 21 THEN pakan ELSE 0 END), 0) / NULLIF(COUNT(CASE WHEN harike BETWEEN 15 AND 21 THEN pakan END), 0) AS Minggu3_pakan, COALESCE(SUM(CASE WHEN harike BETWEEN 22 AND 28 THEN pakan ELSE 0 END), 0) / NULLIF(COUNT(CASE WHEN harike BETWEEN 22 AND 28 THEN pakan END), 0) AS Minggu4_pakan,COALESCE(SUM(CASE WHEN harike BETWEEN 29 AND 35 THEN pakan ELSE 0 END), 0) / NULLIF(COUNT(CASE WHEN harike BETWEEN 29 AND 35 THEN pakan END), 0) AS Minggu5_pakan FROM data', (error, results) => {
        if (error) {
            res.status(500).json({ error: 'Terjadi kesalahan saat mengambil data dari database.' });
        } else {
            if (results.length > 0) {
                const data = [
                    {
                        minggu: 1,
                        rerata_ayam: Number(results[0].Minggu1_ayam).toFixed(2),
                        rerata_pakan: Number(results[0].Minggu1_pakan).toFixed(2)
                    },
                    {
                        minggu: 2,
                        rerata_ayam: Number(results[0].Minggu2_ayam).toFixed(2),
                        rerata_pakan: Number(results[0].Minggu2_pakan).toFixed(2)
                    },
                    {
                        minggu: 3,
                        rerata_ayam: Number(results[0].Minggu3_ayam).toFixed(2),
                        rerata_pakan: Number(results[0].Minggu3_pakan).toFixed(2)
                    },
                    {
                        minggu: 4,
                        rerata_ayam: Number(results[0].Minggu4_ayam).toFixed(2),
                        rerata_pakan: Number(results[0].Minggu4_pakan).toFixed(2)
                    },
                    {
                        minggu: 5,
                        rerata_ayam: Number(results[0].Minggu5_ayam).toFixed(2),
                        rerata_pakan: Number(results[0].Minggu5_pakan).toFixed(2)
                    },
                ];
                res.json(data);
            } else {
                res.status(404).json({ error: 'Data tidak ditemukan.' });
            }
        }
    });
});

// 4. satu data jika dipilih / data-pilih?hari=11
// (harike, tanggal, rerata-ayam-rerata pakan, gambar [link 1, link 2, link 3])

app.get('/data-pilih', (req, res) => {
    const hari = req.query.hari; // Mendapatkan nilai parameter 'hari' dari URL

    connection.query('SELECT harike, DATE_FORMAT(tanggal, "%Y-%m-%d") AS tanggal, FORMAT(AVG(ayam), 0) AS rerata_ayam, AVG(pakan) AS rerata_pakan, GROUP_CONCAT(gambar SEPARATOR \', \') AS gambar FROM data WHERE harike = ? GROUP BY harike, tanggal', [hari], (error, results) => {
        if (error) {
            res.status(500).json({ error: 'Terjadi kesalahan saat mengambil data dari database.' });
        } else {
            if (results.length > 0) {
                const gambarArray = results[0].gambar.split(', ');
                const gambarObjek = gambarArray.map(gambar => ({ image: gambar }));
                
                const data = {
                    harike: results[0].harike,
                    tanggal: results[0].tanggal,
                    rerata_ayam: results[0].rerata_ayam,
                    rerata_pakan: Number(results[0].rerata_pakan).toFixed(2),
                    gambar: gambarObjek
                };

                res.json(data);
            } else {
                res.status(404).json({ error: 'Data tidak ditemukan.' });
            }
        }
    });
});



  //5 tambah periode baru, pindahkan data ke tabel backup, buat tabel data kosong
  // menambahkan data periode baru
  app.post('/panen', (req, res) => {
    // Mendapatkan periode terakhir saat ini dan menambahkannya dengan 1
    let lastPeriode = 0;
    connection.query('SELECT periode FROM tb_periode ORDER BY periode DESC LIMIT 1', (error, results) => {
      if (error) {
        res.status(500).json({ error: 'Terjadi kesalahan saat mengambil periode terakhir.' });
      } else {
        if (results.length > 0) {
          lastPeriode = results[0].periode;
        }
  
        // Mendapatkan tanggal hari ini
        const currentDate = new Date().toISOString().slice(0, 10);
  
        // Menjalankan query untuk memasukkan periode baru
        const query = 'INSERT INTO tb_periode (periode, tanggal_mulai) VALUES (?, ?)';
        const values = [lastPeriode + 1, currentDate];
  
        connection.query(query, values, (error, results) => {
          if (error) {
            res.status(500).json({ error: 'Terjadi kesalahan saat menyimpan data ke database.' });
          } else {
            // Dapatkan tanggal dan jam saat ini
            const currentDate = new Date();
            const dateNow = currentDate.toISOString().slice(0, 10).replace(/-/g, '_');
            const hourNow = currentDate.getHours().toString().padStart(2, '0');
            const minuteNow = currentDate.getMinutes().toString().padStart(2, '0');
            const secondNow = currentDate.getSeconds().toString().padStart(2, '0');
  
            // Buat nama baru untuk tabel
            const newTableName = `data_${dateNow}_${hourNow}_${minuteNow}_${secondNow}`;
  
            // Ubah nama tabel
            const renameQuery = `RENAME TABLE data TO ${newTableName}`;
            connection.query(renameQuery, (error, results) => {
              if (error) {
                res.status(500).json({ error: 'Terjadi kesalahan saat mengubah nama tabel.' });
              } else {
                const createTableQuery = `CREATE TABLE data (
                  no INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                  tanggal DATE,
                  jam TIME,
                  ayam INT,
                  gambar VARCHAR(100),
                  periode INT,
                  harike INT,
                  pakan DECIMAL(5, 2),
                  besok INT,
                  pakan_besok DECIMAL(5, 2)
                )`;
  
                connection.query(createTableQuery, (error, results) => {
                  if (error) {
                    res.status(500).json({ error: 'Terjadi kesalahan saat membuat tabel baru.' });
                  } else {
                    res.status(200).json({ message: 'Periode, nama tabel, dan tabel baru berhasil diperbarui.' });
                  }
                });
              }
            });
          }
        });
      }
    });
  });
  