<script>
const csvUrl = 'https://docs.google.com/spreadsheets/d/e/2PACX-1vSAbS7bw1SORtP8GVyQ95rb7h_JcRJ0sQR6xPB5-aX9vXxgxTUlXCv1oenjnvoBp72Exm69dcoqKaq9/pub?output=csv';

async function loadData() {
    try {
        const response = await fetch(csvUrl);
        const data = await response.text();
        const rows = data.split('\n'); 

        let total = 0;
        const containers = {
            exec: document.getElementById('group-exec'),
            super: document.getElementById('group-super'),
            patrol: document.getElementById('group-patrol')
        };

        // Clear tables
        Object.values(containers).forEach(c => { if(c) c.innerHTML = ''; });

        rows.forEach((row) => {
            // Use regex to split by comma but respect quotes
            const cols = row.split(/,(?=(?:(?:[^"]*"){2})*[^"]*$)/);
            
            // MAP BASED ON YOUR DATA (C, D, E, H)
            // We use .at() or index numbers: C=2, D=3, E=4, H=7
            const name      = cols[2] ? cols[2].replace(/"/g, "").trim() : "";  
            const id        = cols[3] ? cols[3].replace(/"/g, "").trim() : "";  
            const rank      = cols[4] ? cols[4].replace(/"/g, "").trim() : "";  
            const joined    = cols[7] ? cols[7].replace(/"/g, "").trim() : "--"; 
            const status    = cols[8] ? cols[8].replace(/"/g, "").trim() : "Active"; 

            // VALIDATION: Skip if name is empty, or if name IS the word "Name" (header)
            if (name && name.toLowerCase() !== "name" && name.length > 1) {
                total++;
                const tr = document.createElement('tr');
                const sLower = status.toLowerCase();
                const sClass = sLower.includes('active') ? 'status-active' : 'status-other';
                
                tr.innerHTML = `
                    <td class="badge-id" style="width: 10%;">${id}</td>
                    <td style="width: 20%;">${rank}</td>
                    <td style="width: 30%; font-weight: 600; color: #0a192f;">${name}</td>
                    <td style="width: 20%; color: #666;">${joined}</td>
                    <td style="width: 20%;"><span class="tag ${sClass}">${status}</span></td>
                `;

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
        console.error("Error:", e);
    }
}

loadData();
</script>
