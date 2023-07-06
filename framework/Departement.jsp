            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>Departement</title>
            </head>

            <body>
              

                <h2>Insertion Departement</h2>
                <form action="saveDept" enctype="multipart/form-data" method="post"> 
                    <label for="name">Nom Departement</label>
                    <p><input type="text" name="nom_departement"></p>
                    <br>
                    <label for="name">Nombre Departement</label>
                    <p><input type="number" name="nbr_departement"></p>
                    <br>
                    <input type="hidden" name="classe" value="Departement">
                    <input type="file" name="upload" id="">


                    <input type="submit" value="Inserer">
                </form>
            </body>

            </html>