export default function Modal(type, backdrop, modal, showTrigger, url) {
    this.backdrop = backdrop;
    this.modal = modal;
    this.showTrigger = showTrigger;
    this.url = url;
    this.type = type;
    this.set = function() {
        switch (this.type) {
            case "project":
                this.modal.innerHTML = ` 
        <div class='form-container'>
        <h2> Neues Projekt anlegen</h2>
        <form method='post' action=${this.url}>
  <div class="mb-3">
    <label for="projektName" class="form-label">Projekt Name</label>
    <input type="text" class="form-control" id="projekt-name" name='projektName' required="required">
    <div id="proekt-name-help" class="form-text">Geben Sie Ihrem Projekt einen Eindeutigen Namen</div>
  </div>
  <div class="mb-3 form-check">
                    <label for="color">Farbe</label>
                    <select name="color" id="color" style="width: 50px">
                        <option value="#264653" style="background-color: #264653"></option>
                        <option value="#2a9d8f" style="background-color: #2a9d8f"></option>
                        <option value="#e9c46a" style="background-color: #e9c46a"></option>
                        <option value="#f4a261" style="background-color: #f4a261"></option>
                        <option value="#e76f51" style="background-color: #e76f51"></option>
                    </select>
                </div>
  <button type="submit" class="btn btn-success">Projekt hinzufügen</button>
  <button type="button" class="btn btn-outline-secondary" id='project-btn-cancel'> Abbrechen </button>
</form>
</div>`;
                document
                    .getElementById("project-btn-cancel")
                    .addEventListener("click", this.hideBtn.bind(null,this.backdrop,this.modal));

                break;
            case "task":
                this.modal.innerHTML = ` 
        <div class='form-container'>
        <h2> Neuen Task anlegen</h2>
        <form method='post' action=${this.url}>
  <div class="mb-3">
    <label for="taskName" class="form-label">Task Name</label>
    <input type="text" class="form-control" id="taskName" name='taskName' required="required">
  </div>
  <div class="mb-3">
  <label for="taskDesc" class="form-label">Task Beschreibung</label>
  <textarea class="form-control" id="taskDesc" rows="3" name='taskDesc'></textarea>
</div>
  <button type="submit" class="btn btn-success">Task hinzufügen</button>
  <button type="button" class="btn btn-outline-secondary" id='task-btn-cancel'> Abbrechen </button>
</form>
</div>`;
                document.getElementById("modal-container-task").style.height = "400px";
                document
                    .getElementById("task-btn-cancel")
                    .addEventListener("click", this.hideBtn.bind(null,this.backdrop,this.modal));
                break;

            case "settings":
                this.modal.innerHTML = ` 
        <div class='form-container'>
        <h2>Projekt bearbeiten</h2>
        <form method='post' action=${this.url}>
  <div class="mb-3">
    <label for="projektName" class="form-label">Neuer Projekt Name</label>
    <input type="text" class="form-control" id="projekt-name" name='projektName' >
    <div id="proekt-name-help" class="form-text">Geben Sie Ihrem Projekt einen Eindeutigen Namen</div>
  </div>
  <div class="mb-3 form-check">
                    <label for="color">Farbe</label>
                    <select name="color" id="color" style="width: 50px">
                        <option value="#264653" style="background-color: #264653"></option>
                        <option value="#2a9d8f" style="background-color: #2a9d8f"></option>
                        <option value="#e9c46a" style="background-color: #e9c46a"></option>
                        <option value="#f4a261" style="background-color: #f4a261"></option>
                        <option value="#e76f51" style="background-color: #e76f51"></option>
                    </select>
                </div>
  <button type="submit" class="btn btn-danger">Projekt ändern</button>
  <button type="button" class="btn btn-outline-secondary" id='project-settings-btn-cancel'> Abbrechen </button>
</form>
</div>`;
                document
                    .getElementById("project-settings-btn-cancel")
                    .addEventListener("click", this.hideBtn.bind(null,this.backdrop,this.modal));

                break;

            default:
                this.modal.innerHTML = ` 
        <div class='form-container'>
        <h2 style='margin-bottom: 50px;'> Sind Sie sicher das Sie diesen Eintrag löschen wollen ?</h2>
        <form method='post' action=${this.url}>
  <button type="submit" class="btn btn-danger">Löschen</button>
  <button type="button" class="btn btn-outline-secondary" id='confirm-btn-cancel'> Abbrechen </button>
</form>
</div>`;
                document
                    .getElementById("confirm-btn-cancel")
                    .addEventListener("click", this.hideBtn.bind(null,this.backdrop,this.modal));
        }
        this.showTrigger.addEventListener(
            "click",
            this.show.bind(null, this.backdrop, this.modal)
        );
    };

    this.show = function(backdrop, modal) {
        backdrop.style.display = "block";
        modal.style.display = "block";
        backdrop.addEventListener("click", function() {
            backdrop.style.display = "none";
            modal.style.display = "none";
        });
    };
    this.hideBtn = function (backdrop, modal) {
        backdrop.style.display = "none";
        modal.style.display = "none";
    };
}