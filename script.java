<script>
const csvUrl = 'https://docs.google.com/spreadsheets/d/e/2PACX-1vSAbS7bw1SORtP8GVyQ95rb7h_JcRJ0sQR6xPB5-aX9vXxgxTUlXCv1oenjnvoBp72Exm69dcoqKaq9/pub?output=csv&cache=' + Math.random();

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

        // Reset containers
        Object.values(containers).forEach(c => { if(c) c.innerHTML = ''; });

        rows.forEach((row) => {
            const cols = row.split(/,(?=(?:(?:[^"]*"){2})*[^"]*$)/);
            
            // MAPPING: C=2 (Name), D=3 (ID), E=4 (Rank), H=7 (Joined)
            const name      = cols[2] ? cols[2].replace(/"/g, "").trim() : "";
            const id        = cols[3] ? cols[3].replace(/"/g, "").trim() : "";
            const rank      = cols[4] ? cols[4].replace(/"/g, "").trim() : "";
            const joined    = cols[7] ? cols[7].replace(/"/g, "").trim() : "--";
            const status    = cols[8] ? cols[8].replace(/"/g, "").trim() : "Active";

            // THE CLEANER: This ignores the "TRFAIRK9HSB" garbage and headers
            const rawRow = row.toUpperCase();
            const isGarbage = rawRow.includes("TRF") || rawRow.includes("HSB") || rawRow.includes("AIR") || rawRow.includes("K9");
            const isHeader = name.toLowerCase() === "name" || name.toLowerCase() === "personnel";

            // Only show if it looks like a real person
            if (name && !isGarbage && !isHeader && name.length > 2) {
                total++;
                const tr = document.createElement('tr');
                const sClass = status.toLowerCase().includes('active') ? 'status-active' : 'status-other';
                
                tr.innerHTML = `
                    <td class="badge-id" style="width: 15%;">${id}</td>
                    <td style="width: 20%;">${rank}</td>
                    <td style="width: 30%; font-weight: 600;">${name}</td>
                    <td style="width: 20%; color: #666;">${joined}</td>
                    <td style="width: 15%;"><span class="tag ${sClass}">${status}</span></td>
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
        console.error(e);
    }
}

loadData();
</script>
