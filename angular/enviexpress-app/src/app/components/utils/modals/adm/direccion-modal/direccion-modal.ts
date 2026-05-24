import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

// Angular Material Standalone Imports
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-direccion-modal',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './direccion-modal.html',
  styleUrls: ['./direccion-modal.css']
})
export class DireccionModalComponent implements OnInit {
  dirForm: FormGroup;
  url = 'https://localhost:8080/';
  
  selectDataNom: any[] = []; 
  selectDataCom: any[] = []; 
  selectDataDir: any[] = []; 
  selectDataLtr: any[] = [];
  selectDataCar: any[] = [];
  
  selectedOptionNom1: any = "";
  selectedOptionNom2: any = "";
  selectedOptionCom: any = "";
  selectedOptionLtr1: any = "";
  selectedOptionLtr2: any = "";
  selectedOptionLtr3: any = "";
  selectedOptionLtr4: any = "";
  selectedOptionLtr5: any = "";
  selectedOptionLtr6: any = "";
  selectedOptionDir1: any = "";
  selectedOptionDir2: any = "";
  selectedOptionDir3: any = "";
  selectedOptionCar1: any = "";
  selectedOptionCar2: any = "";
  
  num1: any = "";
  num2: any = "";
  num3: any = "";
  complemento: any = "";
  direccion: any = "";
  dirText: any = "";
  dirElements: any = "";

  constructor(
    private http: HttpClient,
    public dialogRef: MatDialogRef<DireccionModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private formBuilder: FormBuilder,
    private dialog: MatDialog
  ) {
    this.dirForm = this.formBuilder.group({
      selectedOptionNom1: new FormControl(''),
      selectedOptionNom2: new FormControl(''),
      selectedOptionCom: new FormControl(''),
      selectedOptionDir1: new FormControl(''),
      selectedOptionDir2: new FormControl(''),
      selectedOptionDir3: new FormControl(''),
      selectedOptionLtr1: new FormControl(''),
      selectedOptionLtr2: new FormControl(''),
      selectedOptionLtr3: new FormControl(''),
      selectedOptionLtr4: new FormControl(''),
      selectedOptionLtr5: new FormControl(''),
      selectedOptionLtr6: new FormControl(''),
      selectedOptionCar1: new FormControl(''),
      selectedOptionCar2: new FormControl(''),
      num1: new FormControl(''),
      num2: new FormControl(''),
      num3: new FormControl(''),
      complemento: new FormControl('')
    });
  }

  ngOnInit(): void {
    this.getTipoNomenclatura().subscribe(
      (nomenclaturas: any[]) => {
        nomenclaturas.forEach(nomenclatura => {
          if (nomenclatura.tipoNomenclatura.indexOf("DIR") > -1) {
            this.selectDataDir.push(nomenclatura);
          } else if (nomenclatura.tipoNomenclatura.indexOf("NOM") > -1) {
            this.selectDataNom.push(nomenclatura);
          } else if (nomenclatura.tipoNomenclatura.indexOf("COM") > -1) {
            this.selectDataCom.push(nomenclatura);
          } else if (nomenclatura.tipoNomenclatura.indexOf("LTR") > -1) {
            this.selectDataLtr.push(nomenclatura);
          } else if (nomenclatura.tipoNomenclatura.indexOf("CAR") > -1) {
            this.selectDataCar.push(nomenclatura);
          }
        });
      }
    );

    if (this.data.direccionData != null) {
      const direccionData = this.data.direccionData;
      this.dirForm.setValue({
        selectedOptionNom1: direccionData.selectedOptionNom1 || '',
        selectedOptionNom2: direccionData.selectedOptionNom2 || '',
        selectedOptionCom: direccionData.selectedOptionCom || '',
        selectedOptionDir1: direccionData.selectedOptionDir1 || '',
        selectedOptionDir2: direccionData.selectedOptionDir2 || '',
        selectedOptionDir3: direccionData.selectedOptionDir3 || '',
        selectedOptionLtr1: direccionData.selectedOptionLtr1 || '',
        selectedOptionLtr2: direccionData.selectedOptionLtr2 || '',
        selectedOptionLtr3: direccionData.selectedOptionLtr3 || '',
        selectedOptionLtr4: direccionData.selectedOptionLtr4 || '',
        selectedOptionLtr5: direccionData.selectedOptionLtr5 || '',
        selectedOptionLtr6: direccionData.selectedOptionLtr6 || '',
        selectedOptionCar1: direccionData.selectedOptionCar1 || '',
        selectedOptionCar2: direccionData.selectedOptionCar2 || '',
        num1: direccionData.num1 || '',
        num2: direccionData.num2 || '',
        num3: direccionData.num3 || '',
        complemento: direccionData.complemento || '',
      });
      
      this.selectedOptionNom1 = direccionData.selectedOptionNom1 || '';
      this.selectedOptionNom2 = direccionData.selectedOptionNom2 || '';
      this.selectedOptionCom = direccionData.selectedOptionCom || '';
      this.selectedOptionDir1 = direccionData.selectedOptionDir1 || '';
      this.selectedOptionDir2 = direccionData.selectedOptionDir2 || '';
      this.selectedOptionDir3 = direccionData.selectedOptionDir3 || '';
      this.selectedOptionLtr1 = direccionData.selectedOptionLtr1 || '';
      this.selectedOptionLtr2 = direccionData.selectedOptionLtr2 || '';
      this.selectedOptionLtr3 = direccionData.selectedOptionLtr3 || '';
      this.selectedOptionLtr4 = direccionData.selectedOptionLtr4 || '';
      this.selectedOptionLtr5 = direccionData.selectedOptionLtr5 || '';
      this.selectedOptionLtr6 = direccionData.selectedOptionLtr6 || '';
      this.selectedOptionCar1 = direccionData.selectedOptionCar1 || '';
      this.selectedOptionCar2 = direccionData.selectedOptionCar2 || '';
      this.num1 = direccionData.num1 || '';
      this.num2 = direccionData.num2 || '';
      this.num3 = direccionData.num3 || '';
      this.complemento = direccionData.complemento || '';
      
      this.direccionComplete();
    }
  }

  getTipoNomenclatura(): Observable<any[]> {
    return this.http.get<any[]>(this.url + "nomenclaturas");
  }

  numberChange(e: any, tipo: string) {
    switch (tipo) {
      case "num1": (e.data == null ? this.num1 = '' : this.num1 += e.data); break;
      case "num2": (e.data == null ? this.num2 = '' : this.num2 += e.data); break;
      case "num3": (e.data == null ? this.num3 = '' : this.num3 += e.data); break;
    }
    this.direccionComplete(); 
  }

  direccionComplete(): void {
    this.dirText = `${this.selectedOptionNom1} ${this.num1} ${this.selectedOptionLtr1}${this.selectedOptionLtr2} ${this.selectedOptionCar1} # ${this.selectedOptionNom2} ${this.num2} ${this.selectedOptionLtr3}${this.selectedOptionLtr4} ${this.selectedOptionCar2} - ${this.num3} ${this.selectedOptionLtr5}${this.selectedOptionLtr6} ${this.selectedOptionDir3} - ${this.selectedOptionCom} ${this.complemento}`.trim().replace(/\s+/g, ' ');
    
    this.dirElements = {
      "selectedOptionNom1": this.selectedOptionNom1,
      "selectedOptionCom": this.selectedOptionCom,
      "selectedOptionDir1": this.selectedOptionDir1,
      "selectedOptionDir2": this.selectedOptionDir2,
      "selectedOptionDir3": this.selectedOptionDir3,
      "selectedOptionLtr1": this.selectedOptionLtr1,
      "selectedOptionLtr2": this.selectedOptionLtr2,
      "selectedOptionLtr3": this.selectedOptionLtr3,
      "selectedOptionLtr4": this.selectedOptionLtr4,
      "selectedOptionLtr5": this.selectedOptionLtr5,
      "selectedOptionLtr6": this.selectedOptionLtr6,
      "selectedOptionCar1": this.selectedOptionCar1,
      "selectedOptionCar2": this.selectedOptionCar2,
      "num1": this.num1,
      "num2": this.num2,
      "num3": this.num3,
      "complemento": this.complemento,
    };
    
    this.direccion = {
      "direccionTexto": this.dirText,
      "direccionMap": this.dirElements
    };
  }
  
  closeDialog(): void {
    this.dialogRef.close(this.direccion);
  }

  submitForm(): void {
    if (this.dirForm.valid) {
      this.dialogRef.close(this.direccion);
    }
  }
}