import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { MenuComponent } from './components/menu.component';
import { PlaceOrderComponent } from './components/place-order.component';
import { ConfirmationComponent } from './components/confirmation.component';

const routes: Routes = [
  { path: '', redirectTo: '/menu', pathMatch: 'full' },
  { path: 'menu', component: MenuComponent },
  { path: 'place-order', component: PlaceOrderComponent },
  { path: 'confirmation', component: ConfirmationComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    PlaceOrderComponent,
    ConfirmationComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes),
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
