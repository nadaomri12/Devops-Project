import { Component } from '@angular/core';

@Component({
  selector: 'app-branding',
  template: `
    <div class="branding" style="display: flex; align-items: center;">
  <a href="/">
    <img
      src="https://www.coconsult.fr/wp-content/uploads/2021/09/CoConsultLOGO-flood2-2.png"
      class="align-middle m-2"
      alt="logo"
      style="width: 90px; height: auto;"
    />
  </a>
  <h2 style="margin-left: 10px; color:#007bff">CO-Audit</h2>
</div>

  `,
})
export class BrandingComponent {
  constructor() {}
}
