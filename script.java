<script>
const csvUrl = 'https://docs.google.com/spreadsheets/d/e/2PACX-1vSAbS7bw1SORtP8GVyQ95rb7h_JcRJ0sQR6xPB5-aX9vXxgxTUlXCv1oenjnvoBp72Exm69dcoqKaq9/pub?output=csv';

async function loadData() {
    try {
        const response = await fetch(csvUrl);
        const data = await response.text();
        const rows = data.split('\n').filter(row => row.trim() !== ''); 

        let total = 0;
        const containers = {
            exec: document.getElementById('group-exec'),
            super: document.getElementById('group-super'),
            patrol: document.getElementById('group-patrol')
        };

        // Reset the tables
        Object.values(containers).forEach(c => { if(c) c.innerHTML = ''; });

        rows.forEach((row, index) => {
            // Skip the first 2 header rows
            if (index < 2) return;

            // Handle commas inside quotes correctly
            const cols = row.split(/,(?=(?:(?:[^"]*"){2})*[^"]*$)/);
            
            // EXACT MAPPING BASED ON YOUR DATA:
            const name      = cols[2] ? cols[2].replace(/"/g, "").trim() : "";  // Col C
            const id        = cols[3] ? cols[3].replace(/"/g, "").trim() : "";  // Col D
            const rank      = cols[4] ? cols[4].replace(/"/g, "").trim() : "";  // Col E
            const joined    = cols[7] ? cols[7].replace(/"/g, "").trim() : "--"; // Col H
            const status    = cols[8] ? cols[8].replace(/"/g, "").trim() : "Active"; // Col I (Assumed)

            if (name && name !== "") {
                total++;
                const tr = document.createElement('tr');
                
                // Color coding for status
                const sLower = status.toLowerCase();
                const sClass = sLower.includes('active') ? 'status-active' : 'status-other';
                
                tr.innerHTML = `
                    <td class="badge-id" style="width: 10%;">${id}</td>
                    <td style="width: 20%;">${rank}</td>
                    <td style="width: 30%; font-weight: 600; color: #0a192f;">${name}</td>
                    <td style="width: 20%; color: #666;">${joined}</td>
                    <td style="width: 20%;"><span class="tag ${sClass}">${status}</span></td>
                `;

                // Sorting into the correct visual sections
                const r = rank.toLowerCase();
                if (r.includes('comm') || r.includes('chief') || r.includes('capt') || r.includes('lieut')) {
                    containers.exec.appendChild(tr);
                } else if (r.includes('sgt') || r.includes('sergeant') || r.includes('corp') || r.includes('cpl')) {
                    containers.super.appendChild(tr);
                } else {
                    containers.patrol.appendChild(tr);
                }
            }
        });

        document.getElementById('counter').innerText = total + " PERSONNEL";
        
    } catch (e) {
        console.error("Data Load Error:", e);
    }
}

loadData();
</script>
